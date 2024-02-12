package dnd.donworry.repository.Support;

import java.util.List;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAInsertClause;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;

@Repository
public abstract class Querydsl4RepositorySupport {
	private final Class domainClass;
	private Querydsl querydsl;
	private EntityManager entityManager;
	private JPAQueryFactory queryFactory;

	public Querydsl4RepositorySupport(Class<?> domainClass) {
		Assert.notNull(domainClass, "Domain class must not be null!");
		this.domainClass = domainClass;
	}

	@PostConstruct
	public void validate() {
		Assert.notNull(entityManager, "EntityManager must not be null!");
		Assert.notNull(querydsl, "Querydsl must not be null!");
		Assert.notNull(queryFactory, "QueryFactory must not be null!");
	}

	protected JPAQueryFactory getQueryFactory() {
		return queryFactory;
	}

	protected Querydsl getQuerydsl() {
		return querydsl;
	}

	protected EntityManager getEntityManager() {
		return entityManager;
	}

	@Autowired
	public void setEntityManager(EntityManager entityManager) {
		Assert.notNull(entityManager, "EntityManager must not be null!");

		JpaEntityInformation entityInformation =
			JpaEntityInformationSupport.getEntityInformation(domainClass, entityManager);
		SimpleEntityPathResolver resolver = SimpleEntityPathResolver.INSTANCE;
		EntityPath path = resolver.createPath(entityInformation.getJavaType());

		this.entityManager = entityManager;
		this.querydsl = new Querydsl(entityManager, new PathBuilder<>(path.getType(), path.getMetadata()));
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	protected <T> JPAQuery<T> select(Expression<T> expr) {
		return getQueryFactory().select(expr);
	}

	protected <T> JPADeleteClause delete(EntityPath<T> from) {
		return getQueryFactory().delete(from);
	}

	protected <T> JPAInsertClause insert(EntityPath<T> from) {
		return getQueryFactory().insert(from);
	}

	protected <T> JPAUpdateClause update(EntityPath<T> from) {
		return getQueryFactory().update(from);
	}

	protected <T> JPAQuery<T> selectFrom(EntityPath<T> from) {
		return getQueryFactory().selectFrom(from);
	}

	protected <T> Page<T> applyPagination(Pageable pageable,
		Function<JPAQueryFactory, JPAQuery> contentQuery) {
		JPAQuery jpaQuery = contentQuery.apply(getQueryFactory());
		List<T> content = getFetch(pageable, jpaQuery);

		return PageableExecutionUtils.getPage(content, pageable, jpaQuery::fetchCount);
	}

	protected <T> Page<T> applyPagination(Pageable pageable,
		Function<JPAQueryFactory, JPAQuery> contentQuery, Function<JPAQueryFactory,
		JPAQuery> countQuery) {
		JPAQuery jpaContentQuery = contentQuery.apply(getQueryFactory());
		List<T> content = getFetch(pageable, jpaContentQuery);
		JPAQuery countResult = countQuery.apply(getQueryFactory());

		return PageableExecutionUtils.getPage(content, pageable, countResult::fetchCount);
	}

	private List getFetch(Pageable pageable, JPAQuery jpaContentQuery) {
		return getQuerydsl().applyPagination(pageable, jpaContentQuery).fetch();
	}
}

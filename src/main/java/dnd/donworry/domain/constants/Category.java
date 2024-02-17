package dnd.donworry.domain.constants;

import lombok.Getter;

@Getter
public enum Category {

	CONGRATULATORY_MONEY("축의금"),
	BRIDAL_SHOWER("브라이덜 샤워"),
	GUEST_LOOK("하객룩"),
	ETC("기타");

	private final String name;

	Category(String name) {
		this.name = name;
	}

	public static Category of(String name) {
		for (Category category : Category.values()) {
			if (category.name.equals(name)) {
				return category;
			}
		}
		return null;
	}
}

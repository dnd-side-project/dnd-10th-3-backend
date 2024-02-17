/*
package dnd.donworry.manager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dnd.donworry.domain.dto.test.TestRequestDto;
import dnd.donworry.domain.dto.test.TestResponseDto;

@ExtendWith(MockitoExtension.class)
public class TestManagerTest {

	@InjectMocks
	private TestManager testManager;

	@Mock
	private TestRequestDto testRequestDto;

	@Test
	public void testMakeResult() {
		// given
		when(testRequestDto.factorList()).thenReturn(new Long[] {1L, 2L, 3L});

		// when
		TestResponseDto result = testManager.makeResult(testRequestDto);

		// then
		assertEquals(3, result.getTemperature());
	}
}*/

package com.question.answer3;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

@SpringBootApplication
public class Answer3Application implements CommandLineRunner {

	private final DuplicateRemoverService duplicateRemoverService;

    public Answer3Application(DuplicateRemoverService duplicateRemoverService) {
        this.duplicateRemoverService = duplicateRemoverService;
    }

	public static void main(String[] args) {
		SpringApplication.run(Answer3Application.class, args);
	}

	@Override
    public void run(String... args) {
        // Test cases
        test(new String[]{"a", "B", "c", "A", "b", "C"}, new String[]{"a", "B", "c"});
        test(new String[]{"apple", "banana", "Apple", "Banana", "APPLE", "BANANA"}, new String[]{"apple", "banana"});
        test(new String[]{"Car", "Bike", "car", "BIKE", "CAR", "bike"}, new String[]{"Car", "Bike"});
        test(new String[]{"A", "B", "C", "a", "b", "c"}, new String[]{"A", "", "C"});
        test(new String[]{"apple", "banana", "carrot", "APPLE", "BANANA", "CARROT", "apple", "banana", "carrot"}, new String[]{"apple", "banana", "carrot"});
    }

    private void test(String[] input, String[] expectedOutput) {
        String[] output = duplicateRemoverService.removeDuplicates(input);
        System.out.println("Input: " + Arrays.toString(input));
        System.out.println("Output: " + Arrays.toString(output));
        System.out.println("Expected Output: " + Arrays.toString(expectedOutput));
        System.out.println(output.equals(Arrays.toString(expectedOutput)) ? "Test Passed" : "Test Failed");
        System.out.println();
    }

}

@Service
class DuplicateRemoverService {

    public String[] removeDuplicates(String[] array) {
        Set<String> seen = new LinkedHashSet<>();
        Set<String> lowercaseSet = new LinkedHashSet<>();

        for (String element : array) {
            String lowerCaseElement = (element instanceof String) ? element.toLowerCase() : element.toString().toLowerCase();
            if (!lowercaseSet.contains(lowerCaseElement)) {
                seen.add(element);
                lowercaseSet.add(lowerCaseElement);
            }
        }

        return seen.toArray(new String[0]);
    }
}

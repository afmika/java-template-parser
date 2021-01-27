import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;

/**
 * @author afmika
 * Basic variable feeding algorithm
 *
 * The very same algorithm runs inside aftemplatejs
 * Refs : https://www.npmjs.com/package/aftemplate
 */
public class Parser {
	
	public static String removeSpaceAndSymbols (String input) {
		Pattern p = Pattern.compile("[\\W\\n\\r\\t]+");
		// get a matcher object
		Matcher m = p.matcher(input); 
		String output = m.replaceAll("");
		
		return output;
	}
	
	public static String feedMappedVariables (String text, HashMap<String, Object> variables) {
		String REGEX = "(\\{\\{[ \\n\\ta-zA-Z0-9]+\\}\\}){1}"; // {{ a_variable }} exactly once
		Pattern p = Pattern.compile(REGEX);

		// get a matcher object
		// average ~ O(n log n)
		Matcher m = p.matcher(text);
		
		// empty buffer to populate
		StringBuffer buffer = new StringBuffer();
		
		// just iterating over each occurences
		while (m.find()){
			
			String occur = m.group(); // first occurence group
			String current = removeSpaceAndSymbols(occur); // '{{  smth }}' -> 'smth'
			
			Object replacement = variables.getOrDefault(current, null);
			System.out.println(current + " => " + replacement); // pro debugging

			if (replacement != null)
				m.appendReplacement(buffer, replacement.toString()); // in place populating ;)
		
		}
		
		m.appendTail(buffer);
		
		return buffer.toString();
	}

	public static void main(String[] args) {

		String text = "Hello {{ name }}"
					+ "\nJust got {{ money }} $ from you ! Thanks";
		
		// variable map
		HashMap<String, Object> variables = new HashMap<>();
		variables.put("name", "Michael");
		variables.put("money", 10000);
		
		String str = Parser.feedMappedVariables (text, variables);
		
		System.out.println(str);
	}
}
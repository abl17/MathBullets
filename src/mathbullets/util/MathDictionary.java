package mathbullets.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The purpose of this is to be used as a tool for Operoids and Arithmetoids to easily make their own operation list
 * @author Austin
 *
 */
public class MathDictionary {
	private int answer;
	private List<String> operators;
	private List<Integer> numberList1;
	private List<Integer> numberList2;
	
	public static final Map<Integer, List<String>> mathMap = new HashMap<>();
	public static final List<String> LIST0 = new ArrayList<>(Arrays.asList(
			"0+0", "0-0", "0*0", "0*1", "0*2", "0*3", "0*4", "0*5", "0*6", "0*7", "0*8", "0*9", "0/1", "0/2",
			"0/3", "0/4", "0/5", "0/6", "0/7", "0/8", "0/9", 
			"1-1", "1*0", "2-2", "2*0", "3-3", "3*0", "4-4", "4*0", "5-5", "5*0", "6-6", "6*0", "7-7", "7*0", 
			"8-8", "8*0", "9-9", "9*0")); 
	public static final List<String> LIST1 = new ArrayList<>(Arrays.asList(
			"0+1", "1+0", "1-0", "1*1", "1/1", "2-1", "2/2", "3-2", "3/3", "4-3", "4/4", "5-4", "5/5", "6-5", 
			"6/6", "7-6", "7/7", "8-7", "8/8", "9-8", "9/9"));
	public static final List<String> LIST2 = new ArrayList<>(Arrays.asList(
			"0+2", "1+1", "1*2", "2+0", "2-0", "2*1", "2/1", "3-1", "4-2", "4/2", "5-3", "6-4", "6/3", "7-5", 
			"8-6", "8/4", "9-7"));
	public static final List<String> LIST3 = new ArrayList<>(Arrays.asList(
			"0+3", "1+2", "1*3", "2+1", "3+0", "3-0", "3*1", "3/1", "4-1", "5-2", "6-3", "6/2", "7-4", "8-5", 
			"9-6", "9/3"));
	public static final List<String> LIST4 = new ArrayList<>(Arrays.asList(
			"0+4", "1+3", "1*4", "2+2", "2*2", "3+1", "4+0", "4-0", "4*1", "4/1", "5-1", "6-2", "7-3", "8-4", 
			"8/2", "9-5"));
	public static final List<String> LIST5 = new ArrayList<>(Arrays.asList(
			"0+5", "1+4", "1*5", "2+3", "3+2", "4+1", "5+0", "5-0", "5*1", "5/1", "6-1", "7-2", "8-3", "9-4"));
	public static final List<String> LIST6 = new ArrayList<>(Arrays.asList(
			"0+6", "1+5", "1*6", "2+4", "2*3", "3+3", "3*2", "4+2", "5+1", "6+0", "6-0", "6*1", "6/1", "7-1", 
			"8-2", "9-3"));
	public static final List<String> LIST7 = new ArrayList<>(Arrays.asList(
			"0+7", "1+6", "1*7", "2+5", "3+4", "4+3", "5+2", "6+1", "7+0", "7-0", "7*1", "7/1", "8-1", "9-2"));
	public static final List<String> LIST8 = new ArrayList<>(Arrays.asList(
			"0+8", "1+7", "1*8", "2+6", "2*4", "3+5", "4+4", "4*2", "5+3", "6+2", "7+1", "8+0", "8-0", "8*1", 
			"8/1", "9-1"));
	public static final List<String> LIST9 = new ArrayList<>(Arrays.asList(
			"0+9", "1+8", "1*9", "2+7", "3+6", "3*3", "4+5", "5+4", "6+3", "7+2", "8+1", "9+0", "9-0", "9*1",
			"9/1"));
	
	/**
	 * Initializes the MathDictionary essentially as a map
	 * @param enteredAnswer
	 */
	public MathDictionary(int enteredAnswer) {
		answer = enteredAnswer;
		mathMap.put(0, LIST0);
		mathMap.put(1, LIST1);
		mathMap.put(2, LIST2);
		mathMap.put(3, LIST3);
		mathMap.put(4, LIST4);
		mathMap.put(5, LIST5);
		mathMap.put(6, LIST6);
		mathMap.put(7, LIST7);
		mathMap.put(8, LIST8);
		mathMap.put(9, LIST9);
	}

	/**
	 * Obtains a certain operation randomly based on the value
	 * @param number
	 * @return
	 */
	public List<String> getOperationList(int number) {
		return mathMap.get(number);
	}
	
	public String generateOperation() {
		String myString = "";
		
		Collections.shuffle(operators);
		Collections.shuffle(numberList1);
		Collections.shuffle(numberList2);
		
		for(String operator:operators) {
			for(int number1:numberList1){
				for(int number2:numberList2){
					int result = -1;
					
					switch (operator) {
					case "+":
						result = number1 + number2;
						break;
					case "-":
						result = number1 - number2;
						break;
					case "*":
						result = number1*number2;
					case "/":
						if (number2 == 0) {break;}
						if (number1%number2 != 0) {break;}
						result = number1/number2;
						break;
					default:
					}
					
					if (result == answer) {
						myString += (Integer.toString(number1) + operator + Integer.toString(number2));
						return myString;
					}
				}
			}
		}
		return myString;
	}
	
	/**
	 * Sets new answer to the math dictionary
	 * @param newAnswer
	 */
	public void setAnswer(int newAnswer) {
		answer = newAnswer;
	}
	
	/**
	 * answer to get from the math dictionary
	 * @return
	 */
	public int getAnswer() {
		return answer;
	}
}

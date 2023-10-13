package fr.blind.anonymous.rbpmn.enforcement;

import fr.blind.anonymous.rbpmn.automata.DFAchar;

public class EnforcerImpl implements Enforcer {

	private Buffer<Character> buffer;
	private String trace;
	private DFAchar property;
	private String outputTrace;

	public EnforcerImpl(String trace, DFAchar property) {

		this.buffer = new Buffer<Character>();
		this.trace = trace;
		this.property = property;

	}

	@Override
	public String enforce() {
		// TODO Auto-generated method stub
		System.out.println("Enforcing rules...");

		String initTrace = this.trace;
		// String inputTrace;
		String outputTrace = this.trace;

		if (property.accepts(initTrace)) {

			this.outputTrace = outputTrace;
			return outputTrace;

		} else {
			// outTrace is outputTrace

			String tempTrace = initTrace;

			StringBuilder sb = new StringBuilder(tempTrace);
			
//			System.out.println("----- Enforcing rules -----");
			System.out.println("----- Enforcing Start -----");
			do {
				System.out.println("----- Enforcing -----");
				for (int i = 1; i <= sb.length(); i++) {

					String inputTrace = sb.substring(0, i);
					String tempOutputTrace = sb.substring(0, i);

					if (!property.accepts(inputTrace)) {
						// buffer.clear();
						
						buffer.add(sb.charAt(i - 1));

						System.out.println("buffer:" + buffer);
						tempTrace = removeCharAt(tempTrace, i - 1);
						System.out.println("NewTrace:" + tempTrace);
						
						tempOutputTrace = sb.substring(0, i-1);
						
						break;

					}
					System.out.println("Input:" + inputTrace + "; Buffer:" + buffer.toString() + "; Output:" + tempOutputTrace);
				}
				sb = new StringBuilder(tempTrace);
				System.out.println("Sb:" + sb.toString());
			} while (!property.accepts(sb.toString()));
			System.out.println("----- Enforcing End -----");
			
			outputTrace = sb.toString();
			this.outputTrace = outputTrace;
			
			outputTrace = this.reorder(outputTrace, this.buffer);
			
			//outputTrace = sb.toString();
			this.outputTrace = outputTrace;
			
			return outputTrace;
		}
	}
	
	private String reorder(String inputString, Buffer buffer) {
		
		
		System.out.println("----- Reordering Start -----");
		System.out.println("-Current Output:-" + inputString);
		System.out.println("-Current Buffer:-" + buffer);
		String input = inputString;
		int size = buffer.size();
		String tempOutput = inputString;
		String output = inputString;
		
		StringBuilder sb = new StringBuilder();
		for(int i=0; i < size; i++) {
			sb.append(buffer.get(i));
		}
		
		int sizeOfsb = sb.length();
		
		while(true) {
		for(int i=0;i < sb.length(); i++) {
			tempOutput = input + sb.charAt(i);
			if(property.accepts(tempOutput)) {
				input = tempOutput;
				buffer.remove(sb.charAt(i));
				sb.deleteCharAt(i);
				output = input;
			}
		}
		
		if(sizeOfsb != sb.length()) {
			sizeOfsb = sb.length();
		}else {
			break;
		}
		
		}
		
		//System.out.println(sb);
	
		
		System.out.println("----- Reordering End -----");
		System.out.println("-Current Output:-" + output);
		System.out.println("-Current Buffer:-" + buffer);
		
		System.out.println("Increased length is (OutputTrace): " + (output.length() - inputString.length()));
		System.out.println("Number of releases is (Buffer):" + (size - buffer.size()));
		
		return output;
	}

	public static String removeCharAt(String s, int pos) {
		return s.substring(0, pos) + s.substring(pos + 1);
	}

	public String toString() {
		return " [Input ]: " + this.trace + "\n [Buffer]:" + this.buffer + "\n [Output]:" + this.outputTrace;
	}
}

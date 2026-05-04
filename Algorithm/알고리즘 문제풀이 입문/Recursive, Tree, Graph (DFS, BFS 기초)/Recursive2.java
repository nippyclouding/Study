import java.util.*;
import java.io.*;

class Recursive2 {
	static int N;
	static StringBuilder st = new StringBuilder("");
	public static void main (String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		N = Integer.parseInt(br.readLine());
		System.out.println(Long.parseLong(solution(N).toString()));
	}
	private static StringBuilder solution(int N) {
		
		if (N / 2 == 0) return st.append(N % 2);
		else {
			StringBuilder s =  solution(N / 2);
			return s.append(String.valueOf(N % 2));
		}  
	}
}

import java.io.*;
import java.util.*;
class Recursive1 {
	static int n;
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		n = Integer.parseInt(br.readLine());
		// 자연수 n 입력 시 재귀 함수로 1 ~ N까지 출력
		System.out.println("1 ~ N"); 
		printOneToN(n);

		
		// 자연수 n 입력 시 재귀 함수로 N ~ 1까지 출력
		System.out.println("N ~ 1");
		printNToOne(n);
	}
	
	private static void printOneToN(int k) {
		if (k == 0) {
			return;
		} else {
			printOneToN(k - 1);
			System.out.println(k);
		}
	}
	private static void printNToOne(int k) {
		if (k == 0) {
			return;
		} else {
			System.out.println(k);
			printNToOne(k - 1);
		}
	}
}

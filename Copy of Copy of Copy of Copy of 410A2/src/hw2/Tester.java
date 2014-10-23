package hw2;

import hw2.TapeDrive;
import hw2.TapeSorter;
public class Tester {
	public static void main(String args[]){
		TapeDrive newTape1= new TapeDrive(14);
		TapeDrive newTape2= new TapeDrive(14);
		int[] a = {49,38,65,97,76,13,24,49,78,34,12,64,1,8};
		int[] b = {2,3,5,7,11,13,17,19,23,29,31,6,4,91};
		
		for (int i=0; i<a.length; i++){
			newTape1.write(a[i]);
		}
		for (int j=0; j<b.length; j++){
			newTape2.write(b[j]);
		}
		
		for (int j=0; j<b.length; j++){
		System.out.print(newTape1.read());
		}
	}
}
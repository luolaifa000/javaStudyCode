package com.test.hello.main;

import java.util.ArrayList;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import jdk.internal.org.objectweb.asm.tree.analysis.Value;

/*
 * 
 * 
 * int[] array = {23,25,24,26,24,27,25,28,21,22};
		ArrayList<Integer> array1 = new ArrayList<Integer>();
		for (int i=0;i<array.length;i++) {
			array1.add( array[i]);
		}
		CountSort countsort = new CountSort(array1);
		countsort.ascSort();
		
		[21, 22, 23, 24, 24, 25, 25, 26, 27, 28]
 */
public class CountSort {
	
	public ArrayList<Integer> unsortArr;
	public ArrayList<Integer> containArr;
	public CountSort(ArrayList<Integer> array)
	{
		unsortArr = array;
		containArr = new ArrayList<Integer>();
	}
	
	public ArrayList<Integer> ascSort()
	{
		int min = unsortArr.get(0);
		int max = unsortArr.get(0);
		for (int i=1;i<unsortArr.size();i++) {
			if (min > unsortArr.get(i)) {
				min = unsortArr.get(i);
			}
			
			if (max < unsortArr.get(i)) {
				max = unsortArr.get(i);
			}
		}
		
		int zone = max-min+1;
		

		for (int j=0;j<zone;j++) {
			containArr.add(0);
		}
		System.out.println(containArr);
		System.out.println(unsortArr.size());
		int index = 0;
		int value = 0;
		for (int m=0;m<unsortArr.size();m++) {
			index = unsortArr.get(m)-min;
			value = containArr.get(index)+1;
			containArr.set(index,value );
			
		}
		ArrayList<Integer> sortarray = new ArrayList<Integer>();
	
		index = 0;
		for (int n=0;n<containArr.size();n++) {
			if (containArr.get(n)>0) {
				for (int k=0;k<containArr.get(n);k++) {
					sortarray.add(min + n);
				}
				
			}
			index++;
		}
		return sortarray;
		
	}
}

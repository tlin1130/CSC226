import java.util.*;
import java.io.*;

public class LinearSelect{

	public static int LS(int[] S, int k){
        if (S.length==1)
        	return S[0];
       
        return linearSelect(0,S.length-1,S,k);
		
	}

    private static int linearSelect(int left, int right, int[] array, int k){
    	//if there is only one element now, just record.
    	if (left >= right){
    		return array[left];
    	}
    	//do the partition 
    	int p = pickCleverPivot(left,right,array);
    	int eIndex = partition(left,right,array,p);
    	//after the partition, do following ops
    	if (k <= eIndex){
    		return linearSelect(left,eIndex-1,array,k);
    	}else if(k == eIndex+1){
    		return array[eIndex];
    	}else{
    		return linearSelect(eIndex+1,right,array,k);
    	}
    }
	
	//do partition with a pivot
    private static int partition(int left, int right, int[] array, int pIndex){
    	//move pivot to last index of the array
    	swap(array,pIndex,right);

    	int p=array[right];
    	int l=left;
    	int r=right-1;
  
    	while(l<=r){
    		while(l<=r && array[l]<=p){
    			l++;
    		}
    		while(l<=r && array[r]>=p){
    			r--;
    		}
    		if (l<r){
    			swap(array,l,r);
    		}
    	}

        swap(array,l,right);
    	return l;
    }

	//Pick a random pivot to do the QuickSelect
	private static int pickRandomPivot(int left, int right){
		int index=0;
		Random rand = new Random();
		index = left+rand.nextInt(right-left+1);
		return index;  
	}
	
	private static int pickCleverPivot(int left, int right, int[] array){
		
		//base-case
		
		if ((right-left+1) <= 5){
			sort(array, left, right);
			if ((right-left+1) == 1){
				return left;
			} else if ((right-left+1) == 2){
				return left;
			} else if ((right-left+1) == 3){
				return left+1;
			} else if((right-left+1) == 4){
				return left+1;
			} else if ((right-left+1) == 5){
				return left+2;
			}
		}
		
		int L = left;
		int M = left + 2;
		int N = (right - left + 1) / 5;
		
		for (int i = 0; i < N; i++){
			sort(array, L, L + 4);
			L = L + 4;			
		}
			
		int frontIndex = left;
		
		for (int i = 0; i < N; i++){
			swap(array, frontIndex, M);
			M = M + 4;
			frontIndex = frontIndex + 1;
		}
		
		if ((right-left+1)%5 != 0){
			int nL = (right-left+1)%5;
			N = N + 1;
			sort(array, right-nL+1, right);
			swap(array, frontIndex, right-(nL/2));		
		}
		
		return pickCleverPivot(left, left+N, array);
	}
	
	//swap two elements in the array
	private static void swap(int[] array, int a, int b){
 		int tmp = array[a];
		array[a] = array[b];
		array[b] = tmp;
	}
	
	//compare two integer values
	private static boolean less(int v, int w){
		if (v < w){
			return true;
		} else{
			return false;
		}	
	}
	
	//partition for sort
	private static int quicksortPartition(int[] a, int lo, int hi){
		int i = lo;
		int j = hi + 1;
		int v = a[lo];
		while (true){
			while (less(a[++i], v)){
				if (i == hi){
					break;
				}
			}
			while (less(v, a[--j])){
				if (j == lo){
					break;
				}
			}
			if (i >= j){
				break;	
			}
			swap(a, i, j);
			
		}
		swap(a, lo, j);
		return j;	
	}
	
	//in-place quicksort
	private static void sort(int[] a, int lo, int hi){
		if (hi <= lo){
			return;
		}
		int j = quicksortPartition(a, lo, hi);
		sort(a, lo, j-1);
		sort(a, j+1, hi);
	}
	
	public static void main(String[] args){
		Scanner s;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			s = new Scanner(System.in);
			System.out.printf("Enter a list of non-negative integers. Enter a negative value to end the list.\n");
		}
		Vector<Integer> inputVector = new Vector<Integer>();

		int v;
		while(s.hasNextInt() && (v = s.nextInt()) >= 0)
			inputVector.add(v);
		
		int k = inputVector.get(0);

		int[] array = new int[inputVector.size()-1];

		for (int i = 0; i < array.length; i++)
			array[i] = inputVector.get(i+1);

		System.out.printf("Read %d values.\n",array.length);


		long startTime = System.nanoTime();

		int kthsmallest = LS(array,k);

		long endTime = System.nanoTime();

		long totalTime = (endTime-startTime);

		System.out.printf("The %d-th smallest element in the input list of size %d is %d.\n",k,array.length,kthsmallest);
		System.out.printf("Total Time (nanoseconds): %d\n",totalTime); 
		
	}
}

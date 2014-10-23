package hw2;

/**
 * Represents a machine with limited memory that can sort tape drives.
 */
public class TapeSorter {

    private int memorySize;
    private int tapeSize;
    public int[] memory;

    public TapeSorter(int memorySize, int tapeSize) {
        this.memorySize = memorySize;
        this.tapeSize = tapeSize;
        this.memory = new int[memorySize];
    }

    /**
     * Sorts the first `size` items in memory via quicksort
     */
    public void quicksort(int size) {
        int[] memoryList = new int[size];
        for(int i=0; i<size; i++){
        	memoryList[i]=memory[i];
        }
        quickSort(memoryList, 0, memoryList.length-1);    
    }

    private static void quickSort(int[] memoryList, int low, int high) {
		if(low<high){
			int middle = getMiddle(memoryList, low, high);
			quickSort(memoryList, 0, middle-1);
			quickSort(memoryList, middle+1, high);
		}
	}

	private static int getMiddle(int[] memoryList, int low, int high) {
		int temp = memoryList[low];
		while(low<high){
			while(low<high && memoryList[high] >= temp){
				high--;
			}
			memoryList[low]=memoryList[high];
			while(low<high && memoryList[low]<=temp){
				low++;
			}
			memoryList[high] = memoryList[low];
		}
		memoryList[low] = temp;
		return low;
	}

	/**
     * Reads in numbers from drive `in` into memory (a chunk), sorts it, then writes it out to a different drive.
     * It writes chunks alternatively to drives `out1` and `out2`.
     *
     * If there are not enough numbers left on drive `in` to fill memory, then it should read numbers until the end of
     * the drive is reached.
     *
     * Example 1: Tape size = 8, memory size = 2
     * ------------------------------------------
     *   BEFORE:
     * in: 4 7 8 6 1 3 5 7
     *
     *   AFTER:
     * out1: 4 7 1 3 _ _ _ _
     * out2: 6 8 5 7 _ _ _ _
     *
     *
     * Example 2: Tape size = 10, memory size = 3
     * ------------------------------------------
     *   BEFORE:
     * in: 6 3 8 9 3 1 0 7 3 5
     *
     *   AFTER:
     * out1: 3 6 8 0 3 7 _ _ _ _
     * out2: 1 3 9 5 _ _ _ _ _ _
     *
     *
     * Example 3: Tape size = 13, memory size = 4
     * ------------------------------------------
     *   BEFORE:
     * in: 6 3 8 9 3 1 0 7 3 5 9 2 4
     *
     *   AFTER:
     * out1: 3 6 8 9 2 3 5 9 _ _ _ _ _
     * out2: 0 1 3 7 4 _ _ _ _ _ _ _ _
     */
    public void initialPass(TapeDrive in, TapeDrive out1, TapeDrive out2) {
    	int tapeNum =(int) Math.ceil(tapeSize/memorySize);
    	int remainder = tapeSize%memorySize;
    	if(remainder==0){
    		for(int i=0; i<tapeNum; i++){
        		for(int j=0; j<memorySize; j++){
        			memory[j] = in.read();
        		}
        		quickSort(memory, 0, memorySize-1);
        		if(i%2==0){
        			for(int k=0; k<memorySize; k++){
        				out1.write(memory[k]);
        			}
        		}else{
        			for(int g=0; g<memorySize; g++){
        				out2.write(memory[g]);
        			}
        		}
    		}
    	}else{
    		for(int i=0; i<tapeNum; i++){
        		for(int j=0; j<memorySize; j++){
        			memory[j] = in.read();	
        		}
        		quickSort(memory, 0, memorySize-1);
        		if(i%2==0){
        			for(int k=0; k<memorySize; k++){
        				out1.write(memory[k]);
        			}
        		}else{
        			for(int g=0; g<memorySize; g++){
        				out2.write(memory[g]);
        			}
        		}
        	}
    		int[] temp2 = new int[remainder];
    		for(int h=0; h<remainder; h++){
    			temp2[h]=in.read();
    		}quickSort(temp2,0,remainder-1);
    		if(tapeNum%2==0){
    			for(int x=0; x<remainder; x++){
    				out1.write(temp2[x]);
    			}	
    		}else{
    			for(int y=0; y<remainder; y++){
    				out2.write(temp2[y]);
    		    }
    		}
    	}
    }

    /**
     * Merges the first chunk on drives `in1` and `in2` and writes the sorted, merged data to drive `out`.
     * The size of the chunk on drive `in1` is `size1`.
     * The size of the chunk on drive `in2` is `size2`.
     *
     *          Example
     *       =============
     *
     *  (BEFORE)
     * in1:  [ ... 1 3 6 8 9 ... ]
     *             ^
     * in2:  [ ... 2 4 5 7 8 ... ]
     *             ^
     * out:  [ ... _ _ _ _ _ ... ]
     *             ^
     * size1: 4, size2: 4
     *
     *   (AFTER)
     * in1:  [ ... 1 3 6 8 9 ... ]
     *                     ^
     * in2:  [ ... 2 4 5 7 8 ... ]
     *                     ^
     * out:  [ ... 1 2 3 4 5 6 7 8 _ _ _ ... ]
     *                             ^
     */
    public void mergeChunks(TapeDrive in1, TapeDrive in2, TapeDrive out, int size1, int size2) {
    	int[] temp = new int[size1+size2+2];
    	int[] result = new int[size1+size2+2];
    	for(int i=0; i<=size1; i++){
    		//System.out.print(in1.read());
    		temp[i]=in1.read();	
    	}
    	
    	for(int j=0; j<=size2; j++){
    		temp[size1+j+1]=in2.read();
    	}
    	
    	mergeSort(temp, result, 0, size1+size2+1);
    	for(int i=0; i<size1+size2+2; i++){
			out.write(temp[i]);
		}
    }
    
    public static void mergeSort(int[]A, int[] B, int left, int right){
    	if(left==right)
    		return;
    	int mid = (left+right)/2;
    	mergeSort(A, B, left, mid);
    	mergeSort(A, B, mid+1, right);
    	merge(A, B, left, mid+1, right);
    }

    private static void merge(int[] a, int[] tmpArray, int leftPos, int rightPos, int rightEnd) {
		int leftEnd = rightPos-1;
		int tmpPos = leftPos;
		int numElements = rightEnd - leftPos + 1;
		while(leftPos <= leftEnd && rightPos <=rightEnd){
			if(a[leftPos]<=a[rightPos]){
				tmpArray[tmpPos++] = a[leftPos++];
			}else{
				tmpArray[tmpPos++] = a[rightPos++];
			}
		}
		while(leftPos <= leftEnd){
			tmpArray[tmpPos++] = a[leftPos++];
		}
		while(rightPos <=rightEnd){
			tmpArray[tmpPos++] = a[rightPos++];
		}
		for(int i=0; i<numElements; i++,rightEnd--){
			a[rightEnd] = tmpArray[rightEnd];
		}
		
	}

	/**
     * Merges chunks from drives `in1` and `in2` and writes the resulting merged chunks alternatively to drives `out1`
     * and `out2`.
     *
     * The `runNumber` argument denotes which run this is, where 0 is the first run.
     *
     * -- Math Help --
     * The chunk size on each drive prior to merging will be: memorySize * (2 ^ runNumber)
     * The number of full chunks on each drive is: floor(tapeSize / (chunk size * 2))
     *   Note: If the number of full chunks is 0, that means that there is a full chunk on drive `in1` and a partial
     *   chunk on drive `in2`.
     * The number of leftovers is: tapeSize - 2 * chunk size * number of full chunks
     *
     * To help you better understand what should be happening, here are some examples of corner cases (chunks are
     * denoted within curly braces {}):
     *
     * -- Even number of chunks --
     * in1 ->   { 1 3 5 6 } { 5 7 8 9 }
     * in2 ->   { 2 3 4 7 } { 3 5 6 9 }
     * out1 ->  { 1 2 3 3 4 5 6 7 }
     * out2 ->  { 3 5 5 6 7 8 9 9 }
     *
     * -- Odd number of chunks --
     * in1 ->   { 1 3 5 } { 6 7 9 } { 3 4 8 }
     * in2 ->   { 2 4 6 } { 2 7 8 } { 0 3 9 }
     * out1 ->  { 1 2 3 4 5 6 } { 0 3 3 4 8 9 }
     * out2 ->  { 2 6 7 7 8 9 }
     *
     * -- Number of leftovers <= the chunk size --
     * in1 ->   { 1 3 5 6 } { 5 7 8 9 }
     * in2 ->   { 2 3 4 7 }
     * out1 ->  { 1 2 3 3 4 5 6 7 }
     * out2 ->  { 5 7 8 9 }
     *
     * -- Number of leftovers > the chunk size --
     * in1 ->   { 1 3 5 6 } { 5 7 8 9 }
     * in2 ->   { 2 3 4 7 } { 3 5 }
     * out1 ->  { 1 2 3 3 4 5 6 7 }
     * out2 ->  { 3 5 5 7 8 9 }
     *
     * -- Number of chunks is 0 --
     * in1 ->   { 2 4 5 8 9 }
     * in2 ->   { 1 5 7 }
     * out1 ->  { 1 2 4 5 5 7 8 9 }
     * out2 ->
     */
    public void doRun(TapeDrive in1, TapeDrive in2, TapeDrive out1, TapeDrive out2, int runNumber) {
    	int chunkSize = (int) (memorySize*Math.pow(2, runNumber));
    	int fullChunks = (int)Math.floor(tapeSize / (chunkSize * 2));
    	int leftOver =  tapeSize - 2 * chunkSize * fullChunks;
    	int[] result = new int[chunkSize*2];
    	
    	if(fullChunks==0){
    		//Number of chunks is 0
    		int[] D = new int[tapeSize];
			int[] E = new int[tapeSize];
    		for(int i=0; i<chunkSize; i++){
				D[i] = in1.read();	
			}
			for(int j=0; j<tapeSize-chunkSize; j++){
				D[chunkSize+j] = in2.read();	
			}
			mergeSort(D, E, 0, tapeSize-1);
			for(int k=0; k<tapeSize; k++){
				out1.write(D[k]);
			
			}
    	}else{
    		//Even number of chunks && Odd number of chunks
    		
    			for(int i=0; i<fullChunks; i++){
    				int[] A = new int[chunkSize*2];
    				for(int j=0; j<chunkSize; j++){
    					A[j] = in1.read();		
    				}
    				for(int j=0; j<chunkSize; j++){
    					A[chunkSize+j] = in2.read();
    				}
    				mergeSort(A, result, 0, chunkSize*2-1);
    				if(i%2==0){
    					for(int k=0; k<chunkSize*2; k++){
    						out1.write(A[k]);
    					}
    				}else{
    					for(int k=0; k<chunkSize*2; k++){
    						out2.write(A[k]);
    					}
    				}
    			}if(tapeSize%(chunkSize*2)!=0){
    				int[] B = new int[leftOver];
        			int[] C = new int[leftOver];
        			if(leftOver<=chunkSize && chunkSize<=memorySize){
        				//Number of leftovers <= the chunk size
        				for(int i=0; i<leftOver; i++){
        					B[i] = in1.read();	
        				}
        				mergeSort(B, C , 0, leftOver-1);
        				for(int k=0; k<leftOver; k++){
        					out2.write(B[k]);
        				}
        			}else{
        				//Number of leftovers > the chunk size
        				for(int i=0; i<memorySize; i++){
        					B[i] = in1.read();
        				}
        				for(int j=0; j<leftOver-memorySize; j++){
        					B[memorySize+j] = in2.read();
        				}
        				mergeSort(B, C , 0, leftOver-1);
        				for(int k=0; k<leftOver; k++){
        					out2.write(B[k]);
        				}
        			}			
    			}
    		}
    	
    }

    /**
     * Sorts the data on drive `t1` using the external sort algorithm. The sorted data should end up on drive `t1`.
     *
     * Initially, drive `t1` is filled to capacity with unsorted numbers.
     * Drives `t2`, `t3`, and `t4` are empty and are to be used in the sorting process.
     */
    public void sort(TapeDrive t1, TapeDrive t2, TapeDrive t3, TapeDrive t4) {
    	
    	int idx = (int)Math.floor(tapeSize / memorySize);
    	if(tapeSize%memorySize!=0){
    		idx = idx+1;
    	}
    	int idx4 =(int)Math.floor(idx / 2);
    	int idx3 =idx4;
    	if(idx4%2!=0){
    		idx3 = idx3+1;
    	}
    	if(idx%2==0){
    		for(int i=0; i<idx4*memorySize; i++){
    			t3.write(t1.read());
    		}
    		for(int j=0; j<tapeSize - idx4*memorySize; j++){
    			t2.write(t1.read());
    		}
    	}else{
    		for(int i=0; i<idx3*memorySize; i++){
    			t2.write(t1.read());
    		}
    		for(int j=0; j<tapeSize - idx3/memorySize; j++){
    			t3.write(t1.read());
    		}
    	}
        int runNum = 0;
        if(idx%2!=0){
        idx=idx+1;
	 	}
        while(idx>2){
         	if(idx%2!=0){
         		idx=idx+1;
         	}
         	idx = idx/2;
         	runNum++;
        }
        doRun(t2,t3,t1,t4,runNum);
        
    }

    public static void main(String[] args) {
        // Example of how to test
        TapeSorter tapeSorter = new TapeSorter(10, 91);
        TapeDrive t1 = TapeDrive.generateRandomTape(91);
        TapeDrive t2 = new TapeDrive(91);
        TapeDrive t3 = new TapeDrive(91);
        TapeDrive t4 = new TapeDrive(91);

        tapeSorter.sort(t1, t2, t3, t4);
        int last = Integer.MIN_VALUE;
        boolean sorted = true;
        for (int i = 0; i < 91; i++) {
            int val = t1.read();
            sorted &= last <= val;
            last = val;
        }
        if (sorted)
            System.out.println("Sorted!");
        else
            System.out.println("Not sorted!");
        
//		TapeDrive newTape1= new TapeDrive(8);
//		TapeDrive newTape2= new TapeDrive(4);
//		TapeDrive newTape3= new TapeDrive(12);
//		TapeDrive newTape4= new TapeDrive(0);
//		
//		TapeSorter newone = new TapeSorter(4,12);
//		int[] a = { 1,3,5,6,5,7,8,9 };
//		int[] b = { 2,3,4,7};
//		
//		for (int i=0; i<a.length; i++){
//			newTape1.write(a[i]);
//		}
//		for (int j=0; j<b.length; j++){
//			newTape2.write(b[j]);
//		}
//		
//		newone.doRun(newTape1, newTape2, newTape3, newTape4,1);
//		for (int j=0; j<12; j++){
//			System.out.print(newTape3.read());
//		}
//		for (int j=0; j<0; j++){
//			System.out.print(newTape4.read());
//		}
//
		TapeDrive newTape5= new TapeDrive(13);
		TapeDrive newTape6= new TapeDrive(8);
		TapeDrive newTape7= new TapeDrive(5);
		TapeDrive newTape8= new TapeDrive(0);
		
		TapeSorter newone2 = new TapeSorter(4,13);
		int[] c = { 6,3,8,9,3,1,0,7,3,5,9,2,4 };
		int[] d = { 2,3,4,7};
//		newone2.memory ={ 6,3,8,9,3,1,0,7,3,5,9,2,4 };
//		for (int i=0; i<c.length; i++){
//			newTape5.write(c[i]);
//		}
//		newone2.quicksort(5);
//		for (int j=0; j<5; j++){
//			System.out.print(newone2.memory[j]);
//		}
//		for (int j=0; j<d.length; j++){
//			newTape2.write(d[j]);
//		}
		newone2.initialPass(newTape5,newTape6,newTape7);
		for (int idx=0; idx<8; idx++){
			System.out.print(newTape6.read());
		}
		for (int j=0; j<5; j++){
			System.out.print(newTape7.read());
		}
		
		
		
//		int[] memory = {2,5,4,8,9};
//	    for (int j=0; j<3; j++){
//			System.out.print(memory[j]);
//		}	
        
//		int[] a = { 1,7,5,6,5,3,8,9 };

	
        
    }

}
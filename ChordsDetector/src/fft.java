// This code was modified from https://en.wikipedia.org/wiki/Cooley%E2%80%93Tukey_FFT_algorithm#C++_Example_Code

public class fft {
	
	// Separate even/odd elements to lower/upper halves of array respectively.
	public static void separate(Complex[] a, int n, int startFrom) {
	    Complex[] b = new Complex[n/2];
		for (int i = 0; i < n/2; i++)
			b[i] = a[startFrom + (i * 2) + 1];	 		 // Copy all even elements to lower-half of a[].
	    for (int i = 0; i < n/2; i++)
	    	a[startFrom + i] = a[startFrom + (i * 2)];   // Copy all odd elements to b.
	    for (int i = 0; i < n/2; i++)    
	        a[startFrom + i + n/2] = b[i];				 // Copy all odd from b to upper-half of a[].
	}

	// N must be a power of 2, or bad things will happen.
	// N input samples in X[] are FFT'd and results left in X[].
	// Because of Nyquist theorem, N samples means 
	// only first N/2 FFT results in X[] are the answer.
	// (upper half of X[] is a reflection with no new information).
	public static void fft2 (Complex[] X, int N, int startFrom) {
	    if (N < 2) {return;} 
	    else {
	        separate(X, N, startFrom);     				// All evens to lower half, all odds to upper half.
	        fft2(X, N/2, startFrom);   	   				// Recurse even items.
	        fft2(X, N/2, startFrom + N/2); 				// Recurse odd items.  			
	        for (int k = 0; k < N/2; k++) {				// Combine results of two half recursions.
	            Complex e = X[startFrom + k 	 ];   	// even
	            Complex o = X[startFrom + k + N/2];   	// odd			   
	            Complex w = new Complex(0.0, -2.0 * Math.PI * + k/N).exp();		// W is the "twiddle-factor".
	            X[startFrom + k		 ] = e.plus(w.times(o));
	            X[startFrom + k + N/2] = e.minus(w.times(o));
	        }
	    }
	}
}


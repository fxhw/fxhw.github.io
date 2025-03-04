class lcgCracker{
    private int[] outputs; // the lcg outputs, len>=5
    private int[] Sn; // Xn+1-Xn
    private int[] Qn; // Sn+1^2 -Sn+2*Sn
    private int mod;
    private int mult;
    private int add;
    private int next;

    public lcgCracker(int[] outputs){
        this.outputs=outputs;
        Sn= new int[outputs.length-1];
        for (int i=0; i<outputs.length-1; i++){
            Sn[i]=outputs[i+1]-outputs[i];
        }
        Qn=new int[Sn.length];
        for (int i=0; i< Sn.length-2; i++){
            Qn[i]=Sn[i+1]*Sn[i+1]-Sn[i+2]*Sn[i];
        }
        mod=gcdArr(Qn); // call gcd of all Qn
        // compute multiplier (a)
        mult = ((outputs[1] - outputs[2]) * modInv((outputs[0] - outputs[1]), mod)) % mod;
        if (mult < 0) mult += mod; // ensure positive

        // compute addend (b)
        add = (outputs[1] - mult * outputs[0]) % mod;
        if (add < 0) add += mod; // ensure positive

        // Predict the next term
        next = (mult * outputs[outputs.length - 1] + add) % mod;
        if (next < 0) next += mod; // ensure positive
    }

    /* returns coefficients of bezouts identity & the gcd. */
    public static int[] extendedgcd(int a, int b){
        if (b==0){
            int[] base = {a, 1, 0}; // the base case has gcd(a,0)=a(1)+0(0)
            return base;
        }
        int[] subdiv = extendedgcd(b, a%b); // break up the problem
        int gcd =subdiv[0];
        int subdivX = subdiv[1];
        int subdivY = subdiv[2];
        int divX = subdivY;
        int divY= subdivX-(a/b)*subdivY;
        int[] div = {gcd, divX, divY};
        return div;
    }

    /* calls gcd on all elements of an array */
    public static int gcdArr(int[] arr) {
        int result = arr[0];
        for (int i : arr) {
            result = Math.abs(extendedgcd(result, i)[0]);
            if (result == 1) return 1;
        }
        return result;
    }

    /* takes the inverse, with precondition of coprimality of parameters */
    public static int modInv(int a, int m){
        a = (a % m + m) % m; // this code is because of java's modulo operator shenanigans.
        int[] result = extendedgcd(a,m);
        int inv = result[1];
        return (inv%m +m) % m;
    }
    public void runCracker(){
        System.out.println("The multiplier is: "+mult);
        System.out.println("The addend is: "+add);
        System.out.println("The modulus is: "+mod);
        System.out.println("And the next term in the lcg is: "+ next);
    }
}

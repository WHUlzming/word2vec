    public Hashtable edgeColors;



    public static final String joinStr = "join";



    public static final String meetStr = "meet";



    public static final String joinSign = "∨";



    public static final String meetSign = "∧";



    boolean convertJoinMeet = true;



    public String name;



    protected InputLattice() {

        super();

    }



    public InputLattice(String file) throws FileNotFoundException, IOException {

package msg;

public enum ClientType {
    PHONE(0,"PHONE"),WEB(1,"WEB");
    private int i;
    private String name;
    private ClientType(int i,String name){
        this.i = i;
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public int getCode(){
        return this.i;
    }
    public static ClientType get(int code){
        switch (code) {
            case 0:
                return ClientType.PHONE;
            case 1:
                return ClientType.WEB;
            default:
                return ClientType.PHONE;
        }
    }
}
package netty;

public class TestEntity {
//	private byte planNo;
//	private byte entryNoSu;
//	private byte entryNo;
//	public byte getPlanNo() {
//		return planNo;
//	}
//	public void setPlanNo(byte planNo) {
//		this.planNo = planNo;
//	}
//	public byte getEntryNoSu() {
//		return entryNoSu;
//	}
//	public void setEntryNoSu(byte entryNoSu) {
//		this.entryNoSu = entryNoSu;
//	}
//	public byte getEntryNo() {
//		return entryNo;
//	}
//	public void setEntryNo(byte entryNo) {
//		this.entryNo = entryNo;
//	}
//	public static void main(String[] args) {
//		TestEntity entity = new TestEntity();
//		byte planNo = -3,b = 4, c = 2;
//		int a = 10;
//		byte d = (byte) a;
//		entity.setPlanNo(planNo);
//		entity.setEntryNoSu(b);
//		entity.setEntryNo(c);
//		System.out.println(JSON.toJSONString(entity));//JsonKit.toJson()
//		
//	}
	public static void main(String[] args) {
		String s ="00-01-04";
		String[] a = s.split("-");
		for(int i = 0;i<a.length;i++) {
			System.out.println(a[i]);
		}
	}
}

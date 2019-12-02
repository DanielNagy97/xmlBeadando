package xml_DOM;

public class Eloado {
	private String nev;
	private String orszag;
	
	public Eloado(String nev, String orszag) {
		this.nev = nev;
		this.orszag = orszag;
	}
	
	public Eloado() {
		this("", "");
	}
	
	public String getNev() {
		return nev;
	}
	public void setNev(String nev) {
		this.nev = nev;
	}
	public String getOrszag() {
		return orszag;
	}
	public void setOrszag(String orszag) {
		this.orszag = orszag;
	}
	@Override
	public String toString() {
		return "Eloado [nev=" + nev + ", orszag=" + orszag + "]";
	}
}
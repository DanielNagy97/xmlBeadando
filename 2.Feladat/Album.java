package xml_DOM;

public class Album {
	private String cim;
	private String mufaj;
	private Integer megjelenes_eve;
	private String eloado;
	private String katalogusszam;
	
	
	public Album(String cim, String mufaj, Integer megjelenes_eve, String eloado, String katalogusszam) {
		this.cim = cim;
		this.mufaj = mufaj;
		this.megjelenes_eve = megjelenes_eve;
		this.eloado = eloado;
		this.katalogusszam = katalogusszam;
	}
	
	public Album() {
		this("", "", 0, "", "");
	}
	
	public String getCim() {
		return cim;
	}
	
	public void setCim(String cim) {
		this.cim = cim;
	}
	
	public String getMufaj() {
		return mufaj;
	}
	
	public void setMufaj(String mufaj) {
		this.mufaj = mufaj;
	}
	
	public Integer getMegjelenes_eve() {
		return megjelenes_eve;
	}
	
	public void setMegjelenes_eve(Integer megjelenes_eve) {
		this.megjelenes_eve = megjelenes_eve;
	}
	
	public String getEloado() {
		return eloado;
	}
	
	public void setEloado(String eloado) {
		this.eloado = eloado;
	}
	
	public String getKatalogusszam() {
		return katalogusszam;
	}
	
	public void setKatalogusszam(String katalogusszam) {
		this.katalogusszam = katalogusszam;
	}
	
	@Override
	public String toString() {
		return String.format("Album[cim=%s, mufaj=%s, megjelenes_eve=%d, eloado=%d, katalogusszam=%s]", 
						cim, mufaj, megjelenes_eve, eloado, katalogusszam);
	}	
}
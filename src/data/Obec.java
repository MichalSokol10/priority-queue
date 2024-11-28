package data;

/**
 * Třída Obec reprezentuje obec s informacemi o číslu kraje, názvu kraje,
 * poštovním směrovacím číslu, názvu obce a o počtech mužů, žen a celkovém počtu
 * obyvatel. Obsahuje metody pro získání a nastavení jednotlivých atributů
 * a přepisuje metodu toString pro čitelný výstup informací o obci.
 */

public class Obec {

    private int cisloKraje;
    private String nazevKraje;
    private int PSC;
    private String obec;
    private int pocetMuzu;
    private int pocetZen;
    private int pocetOsob;

    public Obec(int cisloKraje, String nazevKraje, int PSC, String obec, int pocetMuzu, int pocetZen, int pocetOsob) {
        this.cisloKraje = cisloKraje;
        this.nazevKraje = nazevKraje;
        this.PSC = PSC;
        this.obec = obec;
        this.pocetMuzu = pocetMuzu;
        this.pocetZen = pocetZen;
        this.pocetOsob = pocetOsob;
    }

    public int getCisloKraje() {
        return cisloKraje;
    }

    public void setCisloKraje(int cisloKraje) {
        this.cisloKraje = cisloKraje;
    }

    public int getPSC() {
        return PSC;
    }

    public void setPSC(int PSC) {
        this.PSC = PSC;
    }

    public String getObec() {
        return obec;
    }

    public void setObec(String obec) {
        this.obec = obec;
    }

    public int getPocetMuzu() {
        return pocetMuzu;
    }

    public void setPocetMuzu(int pocetMuzu) {
        this.pocetMuzu = pocetMuzu;
    }

    public int getPocetZen() {
        return pocetZen;
    }

    public void setPocetZen(int pocetZen) {
        this.pocetZen = pocetZen;
    }

    public int getPocetOsob() {
        return pocetOsob;
    }

    public void setPocetOsob(int pocetOsob) {
        this.pocetOsob = pocetOsob;
    }

    public String getNazevKraje() {
        return nazevKraje;
    }

    public void setNazevKraje(String nazevKraje) {
        this.nazevKraje = nazevKraje;
    }
    
    

    @Override
    public String toString() {
        return getObec() +
                "(č. kraje: " + getCisloKraje() +
                ", kraje: " + getNazevKraje() +
                ", psč: " + getPSC() +
                ", počet mužů: " + getPocetMuzu() +
                ", počet žen: " + getPocetZen() + 
                ", počet obyvatel: " + getPocetOsob() +
                ")";
    } 

}

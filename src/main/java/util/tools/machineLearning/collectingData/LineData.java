package util.tools.machineLearning.collectingData;

import network.Circuit;
import network.ControlPlane;
import util.tools.machineLearning.DataSetInterface;

public class LineData implements DataSetInterface {
	
	private int request;
	private String algorithm;
	private double biteRate;
    private String route;
    private int hops;
    private double distance;
    private String mod;
    private String band;
    private int core;
    private double osnr;
    private double deltaOsnr;
    private double deltaOsnrNeigh;
    private double xt;
    private double deltaXT;
    private double deltaXTAdj;
    private int overlaps;
    private double launchPower;
    private double ut;
    private double frag;
    private int wasBlocked;
    private int blockCause;
    
    
    /**
	 * Method that creates the header
	 * 
	 * @return String
	 */
    public String getHeader() {
		StringBuilder buffer = new StringBuilder();
		
		buffer.append("iteration").append(LineDataManager.strSeparator);
		buffer.append("request").append(LineDataManager.strSeparator);
		buffer.append("algorithm").append(LineDataManager.strSeparator);
		buffer.append("biteRate").append(LineDataManager.strSeparator);
		
		buffer.append("route").append(LineDataManager.strSeparator);
		buffer.append("hops").append(LineDataManager.strSeparator);
		buffer.append("distance").append(LineDataManager.strSeparator);
		
		buffer.append("modulation").append(LineDataManager.strSeparator);
		buffer.append("spectrum").append(LineDataManager.strSeparator);
		buffer.append("core").append(LineDataManager.strSeparator);
		
		buffer.append("OSNR(dB)").append(LineDataManager.strSeparator);
		buffer.append("deltaOSNR").append(LineDataManager.strSeparator);
		buffer.append("minDeltaOSNRofNeighbors").append(LineDataManager.strSeparator);
		
		buffer.append("XT(dB)").append(LineDataManager.strSeparator);
		buffer.append("deltaXT").append(LineDataManager.strSeparator);
		buffer.append("minDeltaXTofAdjacents").append(LineDataManager.strSeparator);
		buffer.append("overlaps").append(LineDataManager.strSeparator);
		
		buffer.append("launchPower").append(LineDataManager.strSeparator);
		
		buffer.append("utilization").append(LineDataManager.strSeparator);
		buffer.append("fragmentation").append(LineDataManager.strSeparator);
		
		buffer.append("was_blocked").append(LineDataManager.strSeparator);
		buffer.append("block_cause").append(LineDataManager.strSeparator);
		
		return buffer.toString();
	}
	
	/**
	 * A method that creates a String based on the attributes.
	 * 
	 * @return String
	 */
    public String dataSetToString() {
    	StringBuilder buffer = new StringBuilder();
    	
    	buffer.append(request).append(LineDataManager.strSeparator);
		buffer.append(algorithm).append(LineDataManager.strSeparator);
		buffer.append(biteRate).append(LineDataManager.strSeparator);
		
		buffer.append(route).append(LineDataManager.strSeparator);
		buffer.append(hops).append(LineDataManager.strSeparator);
		buffer.append(distance).append(LineDataManager.strSeparator);
		
		buffer.append(mod).append(LineDataManager.strSeparator);
		buffer.append(band).append(LineDataManager.strSeparator);
		buffer.append(core).append(LineDataManager.strSeparator);
		
		buffer.append(osnr).append(LineDataManager.strSeparator);
		buffer.append(deltaOsnr).append(LineDataManager.strSeparator);
		buffer.append(deltaOsnrNeigh).append(LineDataManager.strSeparator);
		
		buffer.append(xt).append(LineDataManager.strSeparator);
		buffer.append(deltaXT).append(LineDataManager.strSeparator);
		buffer.append(deltaXTAdj).append(LineDataManager.strSeparator);
		buffer.append(overlaps).append(LineDataManager.strSeparator);
		
		buffer.append(launchPower).append(LineDataManager.strSeparator);
		
		buffer.append(ut).append(LineDataManager.strSeparator);
		buffer.append(frag).append(LineDataManager.strSeparator);
		
		buffer.append(wasBlocked).append(LineDataManager.strSeparator);
		buffer.append(blockCause).append(LineDataManager.strSeparator);
		
		return buffer.toString();
    }
    
    /**
	 * This method sets the DataSetStructure
	 * 
	 * @param cp ControlPlane
	 * @param c Circuit
	 */
	public void setDatasetStructure(ControlPlane cp, Circuit c) {
		//
	}

	public void setRequest(int request) {
		this.request = request;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public void setBiteRate(double biteRate) {
		this.biteRate = biteRate;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public void setHops(int hops) {
		this.hops = hops;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public void setMod(String mod) {
		this.mod = mod;
	}

	public void setBand(String band) {
		this.band = band;
	}

	public void setCore(int core) {
		this.core = core;
	}

	public void setOsnr(double osnr) {
		this.osnr = osnr;
	}

	public void setDeltaOsnr(double deltaOsnr) {
		this.deltaOsnr = deltaOsnr;
	}

	public void setDeltaOsnrNeigh(double deltaOsnrNeigh) {
		this.deltaOsnrNeigh = deltaOsnrNeigh;
	}

	public void setXt(double xt) {
		this.xt = xt;
	}

	public void setDeltaXT(double deltaXT) {
		this.deltaXT = deltaXT;
	}

	public void setDeltaXTAdj(double deltaXTAdj) {
		this.deltaXTAdj = deltaXTAdj;
	}

	public void setOverlaps(int overlaps) {
		this.overlaps = overlaps;
	}

	public void setLaunchPower(double launchPower) {
		this.launchPower = launchPower;
	}

	public void setUt(double ut) {
		this.ut = ut;
	}

	public void setFrag(double frag) {
		this.frag = frag;
	}

	public void setWasBlocked(int wasBlocked) {
		this.wasBlocked = wasBlocked;
	}

	public void setBlockCause(int blockCause) {
		this.blockCause = blockCause;
	}
	
	
}

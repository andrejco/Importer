package si.importer.model;

public class FileData implements Comparable<FileData> {

	private long matchId;
	private long marketId;
	private long outcomeId;
	private String specifiers;

	public long getMatchId() {
		return matchId;
	}

	public void setMatchId(long matchId) {
		this.matchId = matchId;
	}

	public long getMarketId() {
		return marketId;
	}

	public void setMarketId(long marketId) {
		this.marketId = marketId;
	}

	public long getOutcomeId() {
		return outcomeId;
	}

	public void setOutcomeId(long outcomeId) {
		this.outcomeId = outcomeId;
	}

	public String getSpecifiers() {
		return specifiers;
	}

	public void setSpecifiers(String specifiers) {
		this.specifiers = specifiers;
	}

	@Override
	public int compareTo(FileData fileData) {
		if (this.matchId == fileData.matchId) {
			return 0;
		} else if (this.matchId > fileData.matchId) {
			return 1;
		} else {
			return -1;
		}
	}

}

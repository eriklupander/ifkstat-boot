package se.ifkgoteborg.stat.importer.controller;

//@Local
public interface SeasonImporter {
//
//	@TransactionAttribute(value=TransactionAttributeType.REQUIRES_NEW)
	void importSeason(String playerData, String season, String data);
}

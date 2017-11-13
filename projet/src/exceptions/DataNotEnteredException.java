package exceptions;

import backEnd.DataRequestStrategy;
import backEnd.DataRequestStrategyCurrent;
import backEnd.DataRequestStrategyHistorical;

public class DataNotEnteredException extends CustomException {
	
	public DataNotEnteredException(DataRequestStrategy dataRequestStrategy) {
		if (dataRequestStrategy instanceof DataRequestStrategyCurrent){
			errorMessage="Make sure you have entered an equity and some fields, please.";
		}else if (dataRequestStrategy instanceof DataRequestStrategyHistorical){
			errorMessage="Make sure you have entered an equity,\n some fields, a start date and an end date, please.";
		}
	}
}

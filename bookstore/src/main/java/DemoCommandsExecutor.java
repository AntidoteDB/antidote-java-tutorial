
import eu.antidotedb.client.AntidoteClient;
import eu.antidotedb.client.Bucket;
import eu.antidotedb.client.CounterRef;

public class DemoCommandsExecutor {
	
	public static int incCounter(AntidoteClient client, String cbucket, String key){
		Bucket<String> bucket = Bucket.create(cbucket);
		CounterRef cnt = bucket.counter(key);
		cnt.increment(client.noTransaction());
		return cnt.read(client.noTransaction());
	}

}

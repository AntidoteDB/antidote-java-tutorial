
import java.util.List;

import eu.antidotedb.client.AntidoteClient;
import eu.antidotedb.client.AntidoteStaticTransaction;
import eu.antidotedb.client.BatchRead;
import eu.antidotedb.client.BatchReadResult;
import eu.antidotedb.client.Bucket;
import eu.antidotedb.client.CounterRef;
import eu.antidotedb.client.InteractiveTransaction;
import eu.antidotedb.client.SetRef;

public class DemoCommandsExecutor {
	
	//increment a counter without wrapping it in a transaction
	public static int incCounter(AntidoteClient client, String bucket, String key){
		Bucket<String> cbucket = Bucket.create(bucket);
		CounterRef cnt = cbucket.counter(key);
		cnt.increment(client.noTransaction());
		return cnt.read(client.noTransaction());
	}
	
	public static int getCounter(AntidoteClient client, String bucket, String key){
		Bucket<String> cbucket = Bucket.create(bucket);
		CounterRef cnt = cbucket.counter(key);
		return cnt.read(client.noTransaction());
	}

	//update a set without wrapping it in a transaction
	public static List<String> addToSet(AntidoteClient client, String bucket, String key, String elem){
		Bucket<String> cbucket = Bucket.create(bucket);
		SetRef<String> set = cbucket.set(key);
		set.add(client.noTransaction(),elem);
		return set.read(client.noTransaction());
	}
	
	public static List<String> getSet(AntidoteClient client, String bucket, String key){
		Bucket<String> cbucket = Bucket.create(bucket);
		SetRef<String> set = cbucket.set(key);
		return set.read(client.noTransaction());
	}
		
	//update and read with in a transaction
	public static String addToSetTxn(AntidoteClient client){
		Bucket<String> bucket = Bucket.create("testbucket");
		CounterRef cnt = bucket.counter("testcounter");
		SetRef<String> set = bucket.set("testset");
		
		try (InteractiveTransaction tx = client.startTransaction()) {
		    cnt.increment(tx);
		    int i = cnt.read(tx);
		    set.add(tx, "i:" + i);
		    tx.commitTransaction();
		}		
		return "ok";		
	}
	
	//batch updates and batch reads
	public static int batchUpdates(AntidoteClient client){
		Bucket<String> bucket = Bucket.create("testbucket");
		CounterRef a = bucket.counter("batchcounter1");
		CounterRef b = bucket.counter("batchcounter2");
		CounterRef c = bucket.counter("batchcounter3");
		
		//increment counters in a batch transaction. 
		AntidoteStaticTransaction tx = client.createStaticTransaction();
		a.increment(tx);
		b.increment(tx);
		c.increment(tx);
		tx.commitTransaction();
		
		//Read multiple keys in batch read
		BatchRead batchRead = client.newBatchRead();
		BatchReadResult<Integer> c1val = a.read(batchRead);
		BatchReadResult<Integer> c2val = b.read(batchRead);
		BatchReadResult<Integer> c3val = c.read(batchRead);
		batchRead.commit();
		int sum = c1val.get() + c2val.get() + c3val.get();	
		return sum;
	}
}

import java.util.List;
import java.util.Objects;

import eu.antidotedb.client.*;

public class DemoCommandsExecutor {
	
	//increment a counter without wrapping it in a transaction
	public static int incCounter(AntidoteClient client, String bucket, String key){
		Bucket cbucket = Bucket.bucket(bucket);
		CounterKey cnt = Key.counter(key);
		cbucket.update(client.noTransaction(),
				cnt.increment(1));
		return cbucket.read(client.noTransaction(), cnt);
	}
	
	public static int getCounter(AntidoteClient client, String bucket, String key){
		Bucket cbucket = Bucket.bucket(bucket);
		CounterKey cnt = Key.counter(key);
		return cbucket.read(client.noTransaction(), cnt);
	}

	//update a set without wrapping it in a transaction
	public static List<String> addToSet(AntidoteClient client, String bucket, String key, String elem){
		Bucket cbucket = Bucket.bucket(bucket);
		SetKey setKey = Key.set(key);
		cbucket.update(client.noTransaction(), setKey.add(elem));
		return (List<String>) cbucket.read(client.noTransaction(), setKey);
	}
	
	public static List<String> getSet(AntidoteClient client, String bucket, String key){
		Bucket cbucket = Bucket.bucket(bucket);
		SetKey setKey = Key.set(key);
		return (List<String>) cbucket.read(client.noTransaction(),setKey);
	}
		
	//update and read with in a transaction
	public static String addToSetTxn(AntidoteClient client){
		Bucket bucket = Bucket.bucket("testbucket");
		ValueCoder<Integer> intCoder = ValueCoder.stringCoder(Objects::toString, Integer::valueOf);
		CounterKey c = Key.counter("testcounter");
		SetKey<Integer> numberSet = Key.set("testset", intCoder);
		try (InteractiveTransaction tx = client.startTransaction()) {
			bucket.update(tx, c.increment(1));
			int i = bucket.read(tx, c);
			bucket.update(tx, numberSet.add(i));
			tx.commitTransaction();
		}
		return "ok";
	}
	
	//batch updates and batch reads
	public static int batchUpdates(AntidoteClient client){
		Bucket bucket = Bucket.bucket("testbucket");
		CounterKey c1 = Key.counter("c1");
		CounterKey c2 = Key.counter("c2");
		CounterKey c3 = Key.counter("c3");

		//increment counters in a batch transaction.
		AntidoteStaticTransaction tx = client.createStaticTransaction();
		bucket.update(tx, c1.increment(1));
		bucket.update(tx, c2.increment(1));
		bucket.update(tx, c3.increment(1));
		tx.commitTransaction();

		//Read multiple keys in batch read
		BatchRead batchRead = client.newBatchRead();
		BatchReadResult<Integer> c1val = bucket.read(batchRead, c1);
		BatchReadResult<Integer> c2val = bucket.read(batchRead, c2);
		BatchReadResult<Integer> c3val = bucket.read(batchRead, c3);
		batchRead.commit(client.noTransaction());
		int sum = c1val.get() + c2val.get() + c3val.get();

		return sum;
	}
}

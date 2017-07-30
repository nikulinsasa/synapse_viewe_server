package sasa.synapse.parser.loader;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Iterator;

import org.junit.Test;

public class DirectoryLoaderTest {

	@Test
	public void test() throws LoaderException {
		DirectoryLoader dl = new DirectoryLoader("tests/default/proxy-services");
		Iterator<File> _files = dl.loadDirectory().getFilesInDirecory();
		
		int count = 0;
		while(_files.hasNext()){
			_files.next();
			count++;
		}
		assertEquals(3, count);
	}

}

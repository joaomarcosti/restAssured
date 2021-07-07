package support;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import tests.IntegrateFlow;
import tests.SchemaTest;

@RunWith(org.junit.runners.Suite.class)
@SuiteClasses({
	
	IntegrateFlow.class,
	SchemaTest.class
})
public class Suite {

}

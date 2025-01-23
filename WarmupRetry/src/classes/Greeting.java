package classes;

import gradingTools.shared.testcases.greeting.GreetingClassRegistry;
public class Greeting implements GreetingClassRegistry{
	@Override
	public Class<?> getGreetingMain() {
		return Hello.class;
	}
}

package registry;

import util.annotations.Tags;
import util.tags.DistributedTags;

@Tags({DistributedTags.REGISTRY, DistributedTags.RMI})
public class Main {
	public static void main(String[] args) {
		(new Registry()).processInit(args);
	}
}

package triangle.codeGenerator.entities;

import triangle.abstractSyntaxTrees.vnames.Vname;
import triangle.codeGenerator.Emitter;
import triangle.codeGenerator.Frame;

public abstract class AddressableEntity extends RuntimeEntity implements FetchableEntity {

	protected final ObjectAddress address;

	protected AddressableEntity(int size, int level, int displacement) {
		super(size);
		address = new ObjectAddress(level, displacement);
	}

	protected AddressableEntity(int size, Frame frame) {
		this(size, frame.getLevel(), frame.getSize());
	}

	public ObjectAddress getAddress() {
		return address;
	}

	public abstract void encodeStore(Emitter emitter, Frame frame, int size, Vname vname);

	public abstract void encodeFetchAddress(Emitter emitter, Frame frame, Vname vname);

	public abstract void encodeFetch(Emitter emitter, Frame frame, int size, Vname vname);
}

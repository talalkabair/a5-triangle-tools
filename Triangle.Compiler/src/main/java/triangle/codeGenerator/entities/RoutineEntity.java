package triangle.codeGenerator.entities;

import triangle.codeGenerator.Emitter;
import triangle.codeGenerator.Frame;

public interface RoutineEntity {

	void encodeCall(Emitter emitter, Frame frame);

	void encodeFetch(Emitter emitter, Frame frame);
}

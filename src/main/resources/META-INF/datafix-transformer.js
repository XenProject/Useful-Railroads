function initializeCoreMod() {

	Opcodes = Java.type("org.objectweb.asm.Opcodes");

	InsnList = Java.type("org.objectweb.asm.tree.InsnList");
	VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode");
	MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");

	INVOKESTATIC = Opcodes.INVOKESTATIC;
	ALOAD = Opcodes.ALOAD;

	return {
		"DataFixesManager#addFixers" : {
			"target" : {
				"type" : "METHOD",
				"class" : "net.minecraft.util.datafix.DataFixesManager",
				"methodName" : "func_210891_a",
				"methodDesc" : "(Lcom/mojang/datafixers/DataFixerBuilder;)V"
			},
			"transformer" : function(methodNode) {
				injectAddFixerCall(methodNode.instructions);
				return methodNode;
			}
		}
	}
}

function injectAddFixerCall(instructions) {
	var injectList = new InsnList();

	injectList.add(new VarInsnNode(ALOAD, 0)); // builder
	injectList.add(new MethodInsnNode(
	// int opcode
	INVOKESTATIC,
	// String owner
	"info/u_team/useful_railroads/hook/DataFixesManagerHook",
	// String name
	"hook",
	// String descriptor
	"(Lcom/mojang/datafixers/DataFixerBuilder;)V",
	// boolean isInterface
	false));

	instructions.insert(injectList);
}

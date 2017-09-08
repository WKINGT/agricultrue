package net.xgs.commons.searcher;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;


public class AnnotationReader {
	private Map<String, Class<? extends Annotation>> annotationMap = new HashMap<String, Class<? extends Annotation>>();


	public void addAnnotation(Class<? extends Annotation> annotation) {
		annotationMap.put('L' + annotation.getName().replace('.', '/') + ';', annotation);
	}

	public boolean isAnnotationed(InputStream inputStream) {
		ClassReader classReader;
		try {
			classReader = new ClassReader(inputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		// ���

		final boolean[] visitorResult = new boolean[]{ false };
		classReader.accept(new ClassVisitor(Opcodes.ASM5) {
			

			@Override
			public AnnotationVisitor visitAnnotation(String annotation, boolean visible) {
				if (annotationMap.containsKey(annotation)) {
					visitorResult[0] = true;
				}
				return super.visitAnnotation(annotation, visible);
			}
			
		}, ClassReader.SKIP_CODE);
		return visitorResult[0];
	}

}
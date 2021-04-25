package gl4;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.jogamp.opengl.GL2ES2.*;

/**
 * @author Alex Micharski
 */
public class GoodbyeSimpleWorld implements GLEventListener, KeyListener {

    private static GLWindow window;
    private static Animator animator;

    float vertices[] = {
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.0f,  0.5f, 0.0f
    };

    int shaderProgram;

    public static void main(String[] args) {
        new GoodbyeSimpleWorld().setup();
    }

    private void setup() {

        GLProfile glProfile = GLProfile.get(GLProfile.GL3);
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);

        window = GLWindow.create(glCapabilities);

        window.setTitle("Goodbye Simple World");
        window.setSize(1024, 768);

        window.addGLEventListener(this);
        window.addKeyListener(this);

        window.setVisible(true);

        window.addGLEventListener(this);
        window.addKeyListener(this);

        window.addWindowListener(new WindowAdapter(){
            @Override
            public void windowDestroyed(WindowEvent e) {
                animator.stop();
                System.exit(0);
            }
        });

        animator = new Animator();
        animator.start();

    }


    @Override
    public void init(GLAutoDrawable drawable) {
        GL4 gl = drawable.getGL().getGL4();

        ShaderCode vertexShader = ShaderCode.create(gl, GL_VERTEX_SHADER, this.getClass(),
                "shaders/gl3", null, "goodbye", "vert", null, true);
        vertexShader.compile(gl);

        ShaderCode fragmentShader = ShaderCode.create(gl, GL_FRAGMENT_SHADER, this.getClass(),
                "shaders/gl3", null, "goodbye", "frag", null, true);
        fragmentShader.compile(gl);

        shaderProgram = gl.glCreateProgram();
        gl.glAttachShader(shaderProgram, vertexShader.id());
        gl.glAttachShader(shaderProgram, fragmentShader.id());
        gl.glLinkProgram(shaderProgram);
        gl.glDeleteShader(vertexShader.id());
        gl.glDeleteShader(fragmentShader.id());

        IntBuffer VBO = GLBuffers.newDirectIntBuffer(1);
        IntBuffer VAO = GLBuffers.newDirectIntBuffer(1);
        gl.glGenVertexArrays(1, VAO);
        gl.glGenBuffers(1, VBO);

        gl.glBindVertexArray(VAO.get());

        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, VBO.get());
        gl.glBufferData(GL.GL_ARRAY_BUFFER, vertices.length, FloatBuffer.wrap(vertices), gl.GL_STATIC_DRAW);

        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 3 * Float.BYTES, 0);
        gl.glEnableVertexAttribArray(0);

        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);

        gl.glBindVertexArray(0);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL4 gl = drawable.getGL().getGL4();
        drawable.setAutoSwapBufferMode(true);
        gl.glUseProgram(shaderProgram);
        gl.glDrawArrays(GL_TRIANGLES, 0, 3);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
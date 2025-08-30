package net.modularmods.service;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

public class RenderService {
    private int framebuffer;
    private int colorTexture;
    private int depthTexture;
    private int triangleVAO;
    private int triangleVBO;
    private int shaderProgram;

    private int framebufferWidth = 800;
    private int framebufferHeight = 600;

    public RenderService() {
        initializeFramebuffer();
        initializeTriangle();
        initializeShaders();
    }

    private void initializeFramebuffer() {
        // Create framebuffer
        framebuffer = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);

        // Create color texture
        colorTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, colorTexture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, framebufferWidth, framebufferHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, (FloatBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colorTexture, 0);

        // Create depth texture
        depthTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, depthTexture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT24, framebufferWidth, framebufferHeight, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (FloatBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthTexture, 0);

        // Check framebuffer completeness
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw new RuntimeException("Framebuffer not complete!");
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    private void initializeTriangle() {
        // Triangle vertices (position + color)
        float[] vertices = {
            // Position     // Color
             0.0f,  0.5f,  1.0f, 0.0f, 0.0f,  // Top vertex - Red
            -0.5f, -0.5f,  0.0f, 1.0f, 0.0f,  // Bottom left - Green
             0.5f, -0.5f,  0.0f, 0.0f, 1.0f   // Bottom right - Blue
        };

        // Create VAO
        triangleVAO = glGenVertexArrays();
        glBindVertexArray(triangleVAO);

        // Create VBO
        triangleVBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, triangleVBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        // Position attribute
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // Color attribute
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 5 * Float.BYTES, 2 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindVertexArray(0);
    }

    private void initializeShaders() {
        String vertexShaderSource = """
            #version 330 core
            layout (location = 0) in vec2 aPos;
            layout (location = 1) in vec3 aColor;
            
            out vec3 vertexColor;
            
            void main() {
                gl_Position = vec4(aPos, 0.0, 1.0);
                vertexColor = aColor;
            }
            """;

        String fragmentShaderSource = """
            #version 330 core
            in vec3 vertexColor;
            out vec4 FragColor;
            
            void main() {
                FragColor = vec4(vertexColor, 1.0);
            }
            """;

        // Compile vertex shader
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexShaderSource);
        glCompileShader(vertexShader);
        checkShaderCompilation(vertexShader, "VERTEX");

        // Compile fragment shader
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);
        checkShaderCompilation(fragmentShader, "FRAGMENT");

        // Create shader program
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);
        checkProgramLinking(shaderProgram);

        // Delete shaders
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    public void renderToFramebuffer() {
        // Bind framebuffer
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
        glViewport(0, 0, framebufferWidth, framebufferHeight);

        // Clear
        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Use shader and draw triangle
        glUseProgram(shaderProgram);
        glBindVertexArray(triangleVAO);
        glDrawArrays(GL_TRIANGLES, 0, 3);

        // Unbind
        glBindVertexArray(0);
        glUseProgram(0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public int getColorTexture() {
        return colorTexture;
    }

    public void resizeFramebuffer(int width, int height) {
        if (width <= 0 || height <= 0) return;

        framebufferWidth = width;
        framebufferHeight = height;

        // Resize color texture
        glBindTexture(GL_TEXTURE_2D, colorTexture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (FloatBuffer) null);

        // Resize depth texture
        glBindTexture(GL_TEXTURE_2D, depthTexture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT24, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (FloatBuffer) null);

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    private void checkShaderCompilation(int shader, String type) {
        IntBuffer success = memAllocInt(1);
        glGetShaderiv(shader, GL_COMPILE_STATUS, success);

        if (success.get(0) == GL_FALSE) {
            String infoLog = glGetShaderInfoLog(shader);
            throw new RuntimeException("Shader compilation failed (" + type + "): " + infoLog);
        }

        memFree(success);
    }

    private void checkProgramLinking(int program) {
        IntBuffer success = memAllocInt(1);
        glGetProgramiv(program, GL_LINK_STATUS, success);

        if (success.get(0) == GL_FALSE) {
            String infoLog = glGetProgramInfoLog(program);
            throw new RuntimeException("Program linking failed: " + infoLog);
        }

        memFree(success);
    }

    public void cleanup() {
        glDeleteVertexArrays(triangleVAO);
        glDeleteBuffers(triangleVBO);
        glDeleteProgram(shaderProgram);
        glDeleteTextures(colorTexture);
        glDeleteTextures(depthTexture);
        glDeleteFramebuffers(framebuffer);
    }
}

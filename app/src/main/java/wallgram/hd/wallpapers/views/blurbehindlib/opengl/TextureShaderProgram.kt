package wallgram.hd.wallpapers.views.blurbehindlib.opengl

import android.content.Context
import android.opengl.GLES20.glGetAttribLocation
import android.opengl.GLES20.glGetUniformLocation
import wallgram.hd.wallpapers.views.blurbehindlib.opengl.ShaderProgram

class TextureShaderProgram(vertexShaderResourceId: Int, fragmentShaderResourceId: Int) : ShaderProgram(vertexShaderResourceId, fragmentShaderResourceId) {

    private var uMatrixLocation: Int = 0
    private var uTextureUnitLocation: Int = 0
    private var uCutoffUnitLocation: Int = 0

    override fun load(context: Context) {
        super.load(context)

        uMatrixLocation = glGetUniformLocation(program, U_MATRIX)
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT)

        positionAttributeLocation = glGetAttribLocation(program, A_POSITION)
        textureCoordinatesAttributeLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES)
        uCutoffUnitLocation = glGetUniformLocation(program, A_CUTOFF_LOCATION)
    }
}

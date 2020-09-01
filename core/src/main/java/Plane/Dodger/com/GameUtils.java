package Plane.Dodger.com;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class GameUtils {
    public static Animation parseSpriteSheet(String fileName, int frameCols, int frameRows,
                                             float frameDuration, Animation.PlayMode mode){
        Texture texture = new Texture(fileName);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        int frameWidth = texture.getWidth() / frameCols;
        int frameHeight = texture.getHeight() / frameRows;

        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);
        TextureRegion[] frames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                frames[index] = temp[i][j];
                index++;
            }
        }

        Array<TextureRegion> framesArray = new Array<TextureRegion>(frames);
        return new Animation(frameDuration, framesArray, mode);
    }

   // creates an Animation from a set of image files
   // name format: fileNamePrefix + N + fileNameSuffix, where
   // 0 <= N <= frameCount

   public static Animation parseImageFiles(String fileNamePrefix, String fileNameSuffix,
                                           int frameCount, float frameDuration, Animation.PlayMode mode){
        TextureRegion[] frames = new TextureRegion[frameCount];

       for (int n = 0; n < frameCount; n++) {
           String fileName = fileNamePrefix + n + fileNameSuffix;
           Texture texture = new Texture(fileName);
           texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
           frames[n] = new TextureRegion(texture);
       }

       Array<TextureRegion> framesArray = new Array<TextureRegion>(frames);
       return new Animation(frameDuration, framesArray, mode);
   }
}

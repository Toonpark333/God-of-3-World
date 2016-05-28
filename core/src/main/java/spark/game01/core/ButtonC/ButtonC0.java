package spark.game01.core.ButtonC;


import playn.core.Layer;
import playn.core.Mouse;
import playn.core.util.Callback;
import spark.game01.core.sprite.Sprite;
import spark.game01.core.sprite.SpriteLoader;

public class ButtonC0 {
    private Sprite sprite;
    public int spriteIndex = 0;
    private boolean hasLoaded = false;
    private float x;
    private float y;
    private String mode;
    String name;

    public ButtonC0(final float x, final float y, final String mode){
        this.x=x;
        this.y=y;
        this.mode = mode;

        this.sprite = SpriteLoader.getSprite("images/buttonc/buttonc.json");
        this.sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite sprite) {

                sprite.layer().setOrigin(sprite.width()/2f,sprite.height()/2f);
                sprite.layer().setTranslation(x,y);
                hasLoaded = true;

                if(mode.equals("b1") == true) {
                    sprite.setSprite(0);
                }else if(mode.equals("b2") == true) {
                    sprite.setSprite(2);
                }else if(mode.equals("b3") == true){
                    sprite.setSprite(4);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
        this.sprite.layer().addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseOver(Mouse.MotionEvent event) {
                super.onMouseOver(event);
                if(mode.equals("b1") == true) {
                    sprite.setSprite(1);
                }else if(mode.equals("b2") == true) {
                    sprite.setSprite(3);
                }else if(mode.equals("b3") == true){
                    sprite.setSprite(5);
                }
            }

            @Override
            public void onMouseOut(Mouse.MotionEvent event) {
                super.onMouseOut(event);
                if(mode.equals("b1") == true) {
                    sprite.setSprite(0);
                }else if(mode.equals("b2") == true) {
                    sprite.setSprite(2);
                }else if(mode.equals("b3") == true){
                    sprite.setSprite(4);
                }
            }
        });
    }

    public Layer layer(){
        return this.sprite.layer();
    }
}

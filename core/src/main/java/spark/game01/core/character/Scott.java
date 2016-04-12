package spark.game01.core.character;


import playn.core.Key;
import playn.core.*;
import playn.core.Keyboard;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.util.Callback;
import spark.game01.core.sprite.Sprite;
import spark.game01.core.sprite.SpriteLoader;


import static playn.core.PlayN.keyboard;

public class Scott {

    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false;
    private  int eventstate;
    private  int rcount=10;
    private  int lcount=10;
    private float x;
    private float y;
    private float z = 24f;
    private int move;



    public enum State{
        IDLE,LIDLE,RUN,LRUN,WALK,LWALK,JUMP,DODGE,ATTK1,ATTK2,ATTK3,
        KICK1,KICK2,JKICK,ULTIK,ULTIB1,ULTIB2,CHARGE,
        DEF,CEL1,CEL2,CEL3,GUITAR,HEADBUTT,LOSE,COMEBACK,
        WASATK1,WASATK2,WASATK3,SLEEP
    };

    private State state = State.IDLE;

    private int e = 0;
    private int offset = 8;

    public Scott(final float x, final float y){
        this.x=x;
        this.y=y;
        PlayN.keyboard().setListener(new Keyboard.Adapter() {
            @Override
            public void onKeyDown(Keyboard.Event event) {
               switch (event.key()) {
                    case UP:
                        if(state == State.CHARGE){
                            state = State.ULTIB1;
                        }
                        if(state == State.SLEEP){
                            state = State.COMEBACK;
                        }
                        if(state == State.ATTK2){
                            state = State.ULTIB1;
                        }
                        break;
                   case LEFT:
                       if(lcount<3&& state == State.LIDLE){
                           state = State.LRUN;
                       }
                       if(state == State.LIDLE && eventstate ==0 || state == State.IDLE && eventstate ==0 || state == State.WALK){
                           state = State.LWALK;
                           lcount=10;
                           rcount=10;
                       }
                       break;
                    case RIGHT:
                        if(rcount<3 && state == State.IDLE){
                            state = State.RUN;
                            move=1;
                        }
                        if(state == State.IDLE && eventstate ==0 || state == State.LIDLE && eventstate ==0 || state == State.LWALK){
                            state = State.WALK;
                            lcount=10;
                            rcount=10;
                            move=1;
                        }
                        if(state == State.IDLE && eventstate == 1){
                            state = State.DODGE;
                            eventstate = 0;
                        }
                        if(state == State.JUMP){
                            move = 1;
                        }
                        break;
                    case DOWN:
                        if(state == State.WALK || state == State.RUN){
                            state = State.DODGE;
                        }
                        if(state == State.IDLE){
                             eventstate = 1;
                        }
                        break;
                    case SPACE:
                        if(state == State.WALK || state == State.RUN || state == State.IDLE){
                            state = State.JUMP;
                        }
                        if(state == State.JUMP && (spriteIndex >= 26 && spriteIndex <=30)){
                            state=State.ULTIK;
                        }
                        if(state == State.SLEEP){
                            state = State.COMEBACK;
                        }
                        break;
                    case A:
                        if(state == State.ATTK2 && (spriteIndex>=48 && spriteIndex<=50)){
                            state = State.ATTK3;
                        }
                        if(state == State.ATTK1 && (spriteIndex>=41 && spriteIndex<=44)){
                            state = State.ATTK2;
                        }
                        if(state == State.IDLE || state == State.WALK||state == State.RUN){
                            state = State.ATTK1;
                        }
                        if(eventstate==1){
                            state = State.HEADBUTT;
                            eventstate=0;
                        }
                        break;
                    case S:
                        if(state == State.JUMP){
                            state = State.JKICK;
                        }
                        if(state == State.KICK1 && (spriteIndex>=127 && spriteIndex<=129)){
                            state = State.KICK2;
                        }
                        if(state == State.IDLE || state == State.WALK || state == State.RUN){
                            state = State.KICK1;
                        }
                        if(state == State.ATTK2){
                            state=State.ULTIB2;
                        }
                        if(state == State.ATTK1){
                            state=State.CHARGE;
                        }
                        break;
                    case D:
                        if(state == State.IDLE || state == State.WALK || state == State.RUN ){
                            state = State.DEF;
                        }
                        break;
                    case NP1:
                        state = State.CEL1;
                        break;
                    case NP2:
                        state = State.CEL2;
                        break;
                    case NP3:
                        state = State.CEL3;
                        break;
                    case NP5:
                        state = State.GUITAR;
                        break;
                    case NP7:
                        state = State.LOSE;
                        break;
                    case NP8:
                        state = State.WASATK1;
                        break;
                    case NP9:
                        state = State.WASATK2;
                        break;
                    case NP6:
                        state = State.WASATK3;
                        break;

               }
            }

            @Override
            public void onKeyUp(Keyboard.Event event){
                switch (event.key()) {
                    case RIGHT:
                        if(state == State.WALK && eventstate == 0){
                            state = State.IDLE;
                            rcount=0;
                        }
                        if(state == State.RUN){
                            state = State.IDLE;
                        }
                        move=0;
                        break;
                    case D:
                        if(state == State.DEF){
                            state = State.IDLE;
                        }
                        break;
                    case LEFT:
                        if(state == State.LWALK && eventstate == 0){
                            state = State.LIDLE;
                            lcount=0;
                        }
                        if(state == State.LRUN){
                            state = State.LIDLE;
                        }
                        move=0;
                        break;
                }
            }
        });


        sprite = SpriteLoader.getSprite("images/scott.json");
        sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width()/2f,sprite.height()/2f);
                sprite.layer().setTranslation(x,y+13f);
                hasLoaded=true;
            }

            @Override
            public void onFailure(Throwable cause) {
                PlayN.log().error("Error loading image!",cause);
            }
        });
    }


    public Layer layer(){
        return sprite.layer();
    }

    public void update(int delta) {
        if(hasLoaded == false) return;
        e = e + delta;
        if(e > 60){
            switch (state) {
                case IDLE:
                    if(!(spriteIndex>=0 && spriteIndex<=7)){
                        spriteIndex=0;
                    }
                    rcount++;
                    System.out.println("Scott Idle = "+spriteIndex);
                    break;
                case WALK:
                    if(!(spriteIndex>=16 && spriteIndex<=21)){
                        spriteIndex=16;
                    }
                    System.out.println("Scott Walk = "+spriteIndex);
                    break;
                case RUN:
                    if(!(spriteIndex>=8 && spriteIndex<=15)){
                        spriteIndex=8;
                    }
                    System.out.println("Scott Run = "+spriteIndex);
                    break;
                case JUMP:
                    if(!(spriteIndex>=22 && spriteIndex<=34)){
                        spriteIndex=22;
                    }
                    System.out.println("Scott Jump = "+spriteIndex);
                    break;
                case DODGE:
                    if(!(spriteIndex>=35 && spriteIndex<=40)){
                        spriteIndex=35;
                    }
                    if(spriteIndex==40){
                        state = State.IDLE;
                    }
                    System.out.println("Scott DODGE = "+spriteIndex);
                    break;
                case ATTK1:
                    if(!(spriteIndex>=41 && spriteIndex<=44)){
                        spriteIndex=41;
                    }
                    if(spriteIndex==44){
                        state = State.IDLE;
                    }
                    System.out.println("Scott ATTK1 = "+spriteIndex);
                    break;
                case ATTK2:
                    if(!(spriteIndex>=45 && spriteIndex<=50)){
                        spriteIndex=45;
                    }
                    if(spriteIndex==50){
                        state = State.IDLE;
                    }
                    System.out.println("Scott ATTK2 = "+spriteIndex);
                    break;
                case ATTK3:
                    if(!(spriteIndex>=51 && spriteIndex<=57)){
                        spriteIndex=51;
                    }
                    if(spriteIndex==57){
                        state = State.IDLE;
                    }
                    System.out.println("Scott ATTK3 = "+spriteIndex);
                    break;
                case DEF:
                    if(!(spriteIndex>=58 && spriteIndex<=64)){
                        spriteIndex=58;
                    }
                    if(spriteIndex>=59){
                        spriteIndex=59;
                    }
                    System.out.println("Scott DEF = "+spriteIndex);
                    break;
                case CEL2:
                    if(!(spriteIndex>=65 && spriteIndex<=79)){
                        spriteIndex=65;
                    }
                    if(spriteIndex==79){
                        state = State.IDLE;
                    }
                    System.out.println("Scott Cel2 = "+spriteIndex);
                    break;
                case GUITAR:
                    if(!(spriteIndex>=80 && spriteIndex<=85)){
                        spriteIndex=80;
                    }
                    System.out.println("Scott Guitar = "+spriteIndex);
                    break;
                case CEL1:
                    if(!(spriteIndex>=86 && spriteIndex<=92)){
                        spriteIndex=86;
                    }
                    if(spriteIndex==92){
                        state = State.IDLE;
                    }
                    System.out.println("Scott CEL1 = "+spriteIndex);
                    break;
                case CEL3:
                    if(!(spriteIndex>=93 && spriteIndex<=110)){
                        spriteIndex=93;
                    }
                    if(spriteIndex==110){
                        state = State.IDLE;
                    }
                    System.out.println("Scott Cel3 = "+spriteIndex);
                    break;
                case ULTIK:
                    if(!(spriteIndex>=111 && spriteIndex<=122)){
                        spriteIndex=111;
                    }
                    System.out.println("Scott Ultikick = "+spriteIndex);
                    break;
                case KICK1:
                    if(!(spriteIndex>=123 && spriteIndex<=129)){
                        spriteIndex=123;
                    }
                    if(spriteIndex==129){
                        state = State.IDLE;
                    }
                    System.out.println("Scott kick1 = "+spriteIndex);
                    break;
                case KICK2:
                    if(!(spriteIndex>=130 && spriteIndex<=136)){
                        spriteIndex=130;
                    }
                    if(spriteIndex==136){
                        state = State.IDLE;
                    }
                    System.out.println("Scott kick2 = "+spriteIndex);
                    break;
                case JKICK:
                    if(!(spriteIndex>=137 && spriteIndex<=143)){
                        spriteIndex=137;
                    }
                    if(spriteIndex==143){
                        state = State.IDLE;
                    }
                    System.out.println("Scott jumpkick = "+spriteIndex);
                    break;
                case ULTIB1:
                    if(!(spriteIndex>=144 && spriteIndex<=155)){
                        spriteIndex=144;
                    }
                    if(spriteIndex==155){
                        state = State.IDLE;
                    }
                    System.out.println("Scott ULTIBOOM1 = "+spriteIndex);
                    break;
                case ULTIB2:
                    if(!(spriteIndex>=156 && spriteIndex<=174)){
                        spriteIndex=156;
                    }
                    if(spriteIndex==174){
                        state = State.IDLE;
                    }
                    System.out.println("Scott ULTIBOOM2 = "+spriteIndex);
                    break;
                case CHARGE:
                    if(!(spriteIndex>=175 && spriteIndex<=191)){
                        spriteIndex=175;
                    }
                    if(spriteIndex==191){
                        state = State.IDLE;
                    }
                    System.out.println("Scott ULTIBOOM2 = "+spriteIndex);
                    break;
                case HEADBUTT:
                    if(!(spriteIndex>=192 && spriteIndex<=201)){
                        spriteIndex=192;
                    }
                    if(spriteIndex==201){
                        state = State.IDLE;
                    }
                    System.out.println("Scott HEADBUTT = "+spriteIndex);
                    break;
                case LOSE:
                    if(!(spriteIndex>=202 && spriteIndex<=205)){
                        spriteIndex=202;
                    }
                    System.out.println("Scott lose = "+spriteIndex);
                    break;
                case COMEBACK:
                    if(!(spriteIndex>=206 && spriteIndex<=212)){
                        spriteIndex=206;
                    }
                    if(spriteIndex==212){
                        state = State.IDLE;
                    }
                    System.out.println("Scott Comeback = "+spriteIndex);
                    break;
                case WASATK1:
                    if(!(spriteIndex>=213 && spriteIndex<=217)){
                        spriteIndex=213;
                    }
                    if(spriteIndex==217){
                        state = State.IDLE;
                    }
                    System.out.println("Scott wasatkf = "+spriteIndex);
                    break;
                case WASATK2:
                    if(!(spriteIndex>=218 && spriteIndex<=221)){
                        spriteIndex=218;
                    }
                    if(spriteIndex==221){
                        state = State.IDLE;
                    }
                    System.out.println("Scott wasatkb = "+spriteIndex);
                    break;
                case WASATK3:
                    if(!(spriteIndex>=222 && spriteIndex<=235)){
                        spriteIndex=222;
                    }
                    if(spriteIndex==235){
                        state = State.SLEEP;
                    }
                    System.out.println("Scott wasatk3 = "+spriteIndex);
                    break;
                case SLEEP:
                    if(!(spriteIndex>=236 && spriteIndex<=237)){
                        spriteIndex=236;
                    }
                    System.out.println("Scott sleep = "+spriteIndex);
                    break;
                case LIDLE:
                    if(!(spriteIndex>=238 && spriteIndex<=245)){
                        spriteIndex=238;
                    }
                    lcount++;
                    System.out.println("Scott lidle = "+spriteIndex);
                    break;
                case LWALK:
                    if(!(spriteIndex>=246 && spriteIndex<=251)){
                        spriteIndex=246;
                    }
                    System.out.println("Scott lwalk = "+spriteIndex);
                    break;
                case LRUN:
                    if(!(spriteIndex>=252 && spriteIndex<=259)){
                        spriteIndex=252;
                    }
                    System.out.println("Scott lrun = "+spriteIndex);
                    break;

            }
            sprite.setSprite(spriteIndex);
            spriteIndex++;
            e=0;
        }
////////////////////////////////////////////////////////////////////////////////////////////////// add Motion on update method
        switch(state){
            case WALK:
                x += 5f;
                sprite.layer().setTranslation(x,y+13f);
                break;
            case LWALK:
                x-=5f;
                sprite.layer().setTranslation(x,y+13f);
                break;
            case RUN:
                x+=10f;
                sprite.layer().setTranslation(x,y+13f);
                break;
            case LRUN:
                x-=10f;
                sprite.layer().setTranslation(x,y+13f);
                break;
            case ATTK1:
                x+=0.5f;
                sprite.layer().setTranslation(x,y+13f);
                break;
            case ATTK2:
                x+=0.5f;
                sprite.layer().setTranslation(x,y+13f);
                break;
            case ATTK3:
                x+=0.5f;
                sprite.layer().setTranslation(x,y+13f);
                break;
            case JKICK:
                x+=8f;
                y = y - z;
                z = z - 2f;
                if(y==320f){
                    z = 24f;
                    state = State.IDLE;
                }
                sprite.layer().setTranslation(x,y+13f);
                break;
            case JUMP:
                if(spriteIndex >= 24) {
                    y = y - z;
                    z = z - 2f;
                    if (y == 320f) {
                        z = 24f;
                        state = State.IDLE;
                    }
                    if(move==1){
                        x+=8f;
                    }
                    sprite.layer().setTranslation(x, y + 13f);
                }
                break;
            case DODGE:
                x+=6f;
                sprite.layer().setTranslation(x,y+13f);
                break;
            case ULTIK:
                x+=10f;
                y = y - z;
                z = z - 2f;
                if(y==320f){
                    z = 24f;
                    state = State.IDLE;
                }
                    sprite.layer().setTranslation(x, y + 13f);
                break;
            case KICK1:
                x+=0.5f;
                sprite.layer().setTranslation(x, y + 13f);
                break;
            case KICK2:
                x+=2.5f;
                sprite.layer().setTranslation(x, y + 13f);
                break;
            case CHARGE:
                if(spriteIndex>=189 && spriteIndex <=191){
                    x+=5f;
                }
                sprite.layer().setTranslation(x, y + 13f);
                break;
            case HEADBUTT:
                if(spriteIndex==197){
                    x+=10f;
                }
                sprite.layer().setTranslation(x, y + 13f);
                break;
            case ULTIB2:
                x+=1f;
                sprite.layer().setTranslation(x, y + 13f);
                break;
        }


////////////////////////////////////////////////////////////////////////////////////////////////// add Motion on update method


    }
}

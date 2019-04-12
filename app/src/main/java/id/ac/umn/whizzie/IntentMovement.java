package id.ac.umn.whizzie;

import android.content.Context;
import android.content.Intent;

public class IntentMovement {
    private Context ctx;

    public IntentMovement(Context ctx) {
        this.ctx = ctx;
    }

    public void moveToTargetNormal(Class target){
        ctx.startActivity(new Intent(ctx, target));
    }
}

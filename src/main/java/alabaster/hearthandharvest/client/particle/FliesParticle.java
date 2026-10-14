package alabaster.hearthandharvest.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class FliesParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private double orbitX, orbitY, orbitZ;
    private float orbitAngle;
    private final float orbitSpeed;
    private final float orbitRadius;
    private final float driftY;

    public FliesParticle(ClientLevel world, double x, double y, double z, SpriteSet sprites) {
        super(world, x, y, z);
        this.sprites = sprites;
        this.setSprite(sprites.get(random));
        this.lifetime = 60 + random.nextInt(80);
        this.hasPhysics = false;
        this.gravity = 0f;
        this.quadSize = 0.04f + random.nextFloat() * 0.03f;

        this.orbitX = x;
        this.orbitY = y;
        this.orbitZ = z;
        this.orbitAngle = random.nextFloat() * Mth.TWO_PI;
        this.orbitSpeed = (random.nextBoolean() ? 1f : -1f) * (0.1f + random.nextFloat() * 0.15f);
        this.orbitRadius = 0.08f + random.nextFloat() * 0.2f;
        this.driftY = 0.003f + random.nextFloat() * 0.006f;

        this.x = orbitX + Math.cos(orbitAngle) * orbitRadius;
        this.z = orbitZ + Math.sin(orbitAngle) * orbitRadius;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        this.orbitAngle += this.orbitSpeed;
        float wobbledRadius = this.orbitRadius + Mth.sin(this.age * 0.5f) * 0.025f;
        this.orbitY += this.driftY;

        this.x = this.orbitX + Math.cos(this.orbitAngle) * wobbledRadius;
        this.y = this.orbitY;
        this.z = this.orbitZ + Math.sin(this.orbitAngle) * wobbledRadius;

        float lifeRatio = (float) this.age / this.lifetime;
        if (lifeRatio > 0.75f) {
            this.alpha = 1f - (lifeRatio - 0.75f) / 0.25f;
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double xd, double yd, double zd) {
            return new FliesParticle(world, x, y, z, sprites);
        }
    }
}
package alabaster.hearthandharvest.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class FeatherParticle extends TextureSheetParticle {
    private final float swaySpeed;
    private final float swayStrength;
    private final float swayPhase;
    private final float spinSpeed;
    private double driftX;
    private double driftZ;

    public FeatherParticle(ClientLevel world, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
        super(world, x, y, z);
        this.setSprite(sprites.get(random));
        this.lifetime = 70 + random.nextInt(70);
        this.hasPhysics = true;
        this.friction = 0.96F;
        this.gravity = 0.02F;
        this.quadSize = 0.06F + random.nextFloat() * 0.04F;

        this.xd = xd * 0.4D + (random.nextDouble() - 0.5D) * 0.02D;
        this.yd = yd * 0.4D + random.nextDouble() * 0.01D;
        this.zd = zd * 0.4D + (random.nextDouble() - 0.5D) * 0.02D;

        this.driftX = this.xd;
        this.driftZ = this.zd;

        this.swaySpeed = 0.15F + random.nextFloat() * 0.15F;
        this.swayStrength = 0.015F + random.nextFloat() * 0.015F;
        this.swayPhase = random.nextFloat() * Mth.TWO_PI;
        this.spinSpeed = (random.nextBoolean() ? 1.0F : -1.0F) * (0.05F + random.nextFloat() * 0.1F);
        this.roll = random.nextFloat() * Mth.TWO_PI;
    }

    @Override
    public void tick() {
        this.oRoll = this.roll;

        if (this.onGround) {
            this.driftX = 0.0D;
            this.driftZ = 0.0D;
            this.xd = 0.0D;
            this.zd = 0.0D;
        } else {
            this.roll += this.spinSpeed;
            this.driftX *= this.friction;
            this.driftZ *= this.friction;

            float phase = this.age * this.swaySpeed + this.swayPhase;
            this.xd = this.driftX + Mth.cos(phase) * this.swayStrength;
            this.zd = this.driftZ + Mth.sin(phase) * this.swayStrength;
            this.yd = Math.max(this.yd, -0.035D);
        }

        super.tick();

        float lifeRatio = (float) this.age / this.lifetime;
        if (lifeRatio > 0.7F) {
            this.alpha = 1.0F - (lifeRatio - 0.7F) / 0.3F;
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
            return new FeatherParticle(world, x, y, z, xd, yd, zd, sprites);
        }
    }
}
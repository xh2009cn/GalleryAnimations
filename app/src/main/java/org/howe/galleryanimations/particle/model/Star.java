package org.howe.galleryanimations.particle.model;

/**
 *
 * 粒子属性类
 *
 */
public class Star {
    public boolean active = true; //是否激活状态
    public float live = 1;//粒子生命
    public float fade = 0.035f; //衰减速度
    public float scale = 1f;

    public float x;  //x位置
    public float y;  //y位置

    public float xi; // x方向
    public float yi; // y方向

    public float rotate; //旋转角度

    public Star clone() {
        Star star = new Star();
        star.live = live;
        star.active = active;
        star.fade = fade;
        star.x = x;
        star.y = y;
        star.xi = xi;
        star.yi = yi;
        star.rotate = rotate;
        star.scale = scale;
        return star;
    }
}

package com.cldprods.test.core;

public interface ILogic {

    void init() throws Exception;

    void update();

    void render();

    void input();

    void cleanup();
}

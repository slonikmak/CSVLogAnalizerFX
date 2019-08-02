package com.oceanos.csvloganalizer;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * @autor slonikmak on 02.08.2019.
 */
public class MainGuiceModule extends AbstractModule {
    @Override
    protected void configure(){
        bind(Repository.class).to(DataSetRepository.class).in(Scopes.SINGLETON);
    }
}

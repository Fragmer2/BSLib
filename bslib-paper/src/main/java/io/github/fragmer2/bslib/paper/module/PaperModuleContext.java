package io.github.fragmer2.bslib.paper.module;

import io.github.fragmer2.bslib.api.di.ServiceContainer;

/**
 * Provides context to PaperModules during initialization.
 */
public record PaperModuleContext(ServiceContainer container) {
}

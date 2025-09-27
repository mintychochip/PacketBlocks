package org.aincraft.commands;

import com.google.inject.PrivateModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public final class CommandModule extends PrivateModule {

  @Override
  protected void configure() {
    Multibinder<PacketBlockCommand> binder = Multibinder.newSetBinder(binder(),
        new TypeLiteral<>() {
        });
    binder.addBinding().to(GetItemCommandImpl.class);
    binder.addBinding().to(InfoCommandImpl.class);
    bind(RootCommand.class).in(Singleton.class);
    expose(RootCommand.class);
  }
}

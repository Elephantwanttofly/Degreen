from tensorflow.keras.optimizers import RMSprop

Model.compile(loss='categorical_crossentropy',
              optimizer=RMSprop(learning_rate=0.0005),
              metrics=['acc'])
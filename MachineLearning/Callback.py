class myCallback(tf.keras.callbacks.Callback):
  def on_epoch_end(self, epoch, logs={}):
    if(logs.get('val_acc') > 0.92 and logs.get('acc') > 0.92): # Experiment with changing this value
      print("\nReached 95% Validation Accuracy so cancelling training!")
      self.model.stop_training = True

callbacks = myCallback()
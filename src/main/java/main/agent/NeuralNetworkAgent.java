package main.agent;

import ai.netadapter.GameDecorator;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import nn.genetic.GeneticAlgorithmBatch;
import nn.neuralnet.NeuralNetwork;

public class NeuralNetworkAgent extends Agent {

  private GeneticAlgorithmBatch<GameDecorator> batch;

  @Override
  public void build() {
    super.build();
    batch = new GeneticAlgorithmBatch<GameDecorator>(
        GameDecorator.class, new NeuralNetwork(config.getLayerConfiguration()).setLearningRate(config.getRandomizationRate()), config.getPopulationSize(), config.getGenerationCount()
    );
    timeline = new Timeline(new KeyFrame(Duration.millis(speed), event -> {
      if (decorator == null) {
        NeuralNetwork neuralNet = batch.processGeneration();
        if (neuralNet != null) {
          decorator = new GameDecorator(neuralNet);
          state.setGame(decorator.getGame());
          state.setNeuralNetwork(decorator.getNeuralNetwork());
        }
      }

      if (decorator == null) {
        stopTimer();
        return;
      }

      decorator.perform();

      if (!decorator.isActive()) {
        decorator = null;
      }
    }));
    timeline.setCycleCount(Animation.INDEFINITE);
    state.setTimeline(timeline);
    timeline.play();
  }

}

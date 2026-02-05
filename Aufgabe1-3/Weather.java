import java.util.Random;

/**
 * Models the weather conditions for the simulation as an object.
 *<p>
 * Abstraction:
 * Each instance of this class represents the weather progression and manages state variables
 * such as humidity and daily sunshine duration. The abstraction encapsulates the logic
 * for simulating weather changes from one day to the next.
 * </p>
 */
public class Weather {
    /**
     * class invariants:
     * sunlight >= 0
     * humidity >= 0 && humidity <= 0
     * totalSunlight >= 0
     * private double latitude ∈ [-PI/2, PI/2]
     * day >= 1 && day <= 240
     * cloudCover >= 0 && cloudCover <= 1
     * random != null
     * annualRainfall >= 0
     * climateHumidity >= 0 && climateHumidity <= 1
     */

    // STYLE: object-oriented: encapsulates weather conditions and methods for daily updates
    private double sunlight;
    private double humidity;
    private double totalSunlight;
    private double latitude;
    private int day = 1;

    private double temperature;
    private double cloudCover;
    private final Random random = new Random();

    private final double annualRainfall; // mm/year
    private final double climateHumidity;


    /**
     * Initializes the weather with an initial humidity value.
     * @param humidity the starting humidity (between 0 and 1).
     * Default climate: 600mm annual rainfall, climateHumidity: Mediterranean (0.3), temperate (0.5), rainforest (0.8)

     * Preconditions: - Ensured by class invariants.
     * Postconditions: - Initializes the weather with the given humidity and latitude
     */
    //STYLE: Object oriented.
    public Weather (double humidity, double latitude) {
        this(humidity, latitude, 600.0, 0.5);
    }

    /**
     * Extended constructor with climate parameters.

     * Preconditions: - Ensured by class invariants
     * Postconditions:
     * - Initializes the waether with the given humidity, latitude, annualRainfall and climateHumidity.
     * - cloudCover is set to a value within the interval of [0.3, 0.7].
     * - the temperature is set to a value with the function calculateBaseTemperature().
     */
    //SYTLE: Object oriented.
    public Weather(double humidity, double latitude, double annualRainfall, double climateHumidity){
        this.humidity = humidity;
        this.latitude = Math.toRadians(latitude);
        this.annualRainfall = annualRainfall;
        this.climateHumidity = climateHumidity;
        this.cloudCover = 0.3 + 0.4 * random.nextDouble();
        this.temperature = calculateBaseTemperature();
    }

    /**
     * Calculates base temperature depending on season and latitude.

     * Postconditions:
     * - The returned value is calculated according to the current day and latitude.
     * - The returned value is calculated according to the formula: 5 + latitudeEffect + seasonalVariation.
     * - seasonalVariation lies within the interval of [-15, +15].
     */
    //STLYE: procedural, sequential computation.
    private double calculateBaseTemperature() {
        double seasonalVariation = 15 * Math.sin((2 * Math.PI / 240) * day - Math.PI / 2);
        double latitudeEffect = 30 * (1 - Math.abs(latitude) / (Math.PI / 2));

        return 5 + latitudeEffect + seasonalVariation;
    }

    /**
     * Calculates simplified potential evaporation (Penman-Monteith inspired).

     * Postconditions:
     * - The returned value is calculated according to the sunlight, cloudCover and temperature.
     * - The returned value is calculated according to the formula: netRadiation * temperatureEffect * 0.001.
     */
    //STYLE: procedural, sequential computation.
    private double calculateEvaporation() {
        double netRadiation = sunlight * (1 - cloudCover * 0.8);
        double temperatureEffect = 0.1 * Math.exp(0.07 * temperature);

        return netRadiation * temperatureEffect * 0.001;
    }

    /**
     * Calculates precipitation based on humidity, temperature, and season.

     * Postconditions:
     * - The returned value is calculated according to humidity, climateHumidity, temperature and annualRainfall.
     * - In the case of a rain event (random.nextDouble() < precipitationProbability) the returned value is within the interval of [(annualRainfall / 240) * 0.5 * 0.001, (annualRainfall / 240) * 1.5 * 0.001].
     */
    //STYLE: procedural sequential computation with if case.
    private double calculatePrecipitation() {
        double basedProbability = humidity * 0.3 + climateHumidity * 0.2;
        double convectiveEffect = Math.max(0, (temperature - 20) * 0.02);
        double seasonalEffect = 0.1 * Math.sin(2 * Math.PI / 240);
        double precipitationProbability = basedProbability + convectiveEffect + seasonalEffect;

        if (random.nextDouble() < precipitationProbability) {
            double dailyRainfall = (annualRainfall / 240) * (0.5 + random.nextDouble());
            return dailyRainfall * 0.001;
        }
        return 0.0;
    }

    /**
     * Simulates the weather for the next day with smoother tansitions.
     * <p>
     * Sunshine hours are determined randomly(between 0 and 12), and humidity is slightly varied.</p>

     * Postcondtions:
     * - temperature is calculated accordint to the temperature of the previous day, targetTemperature and randomTempVariation.
     * - cloudCover is calculated according to the cloudCover of the previous day and targetCloudCover.
     * - sunlight is updated to a value within the interval of [0, 24] according to latitude, cloudCover.
     * - totalSunlight is updated by being incremented by the new sunlight value.
     * - humidity is updated according to the evaporation, precipitation and the humidity value of the previous day. The value of the new humidity conforms to the class invariants.
     * - day is incremented by 1, with the consideration of the class invariants.
     * - All the updated values are conforming to the class invariants.
     */
    //STYLE: procedural, sequential computation with randomization.
    public void nextDayWeather() {
        double targetTemperature = calculateBaseTemperature();
        double randomTempVariation = random.nextGaussian() * 3;
        temperature = 0.8 * temperature + 0.2 * (targetTemperature + randomTempVariation);

        // Cloud cover
        double targetCloudCover = 0.3 + 0.4 * random.nextDouble();
        cloudCover = 0.7 * cloudCover + 0.3 * targetCloudCover;

        // Sunlight hours
        double seasonalVariation = 4 * Math.sin((2 * Math.PI / 240) * day - Math.PI / 2);
        double latitudeFactor = Math.cos(latitude);
        double cloudReduction = cloudCover * 0.8;
        double baseSunlight = 12 + seasonalVariation * latitudeFactor;
        double sunlightVariation = random.nextGaussian() * 2;
        sunlight = Math.max(0, Math.min(24, baseSunlight + sunlightVariation - cloudReduction));
        totalSunlight += sunlight;

        // Moisture dynamics
        double evaporation = calculateEvaporation();
        double precipitation = calculatePrecipitation();
        double moistureChanges = precipitation - evaporation * humidity;
        humidity += moistureChanges;

        // Climatic baseline
        humidity = 0.95 * humidity + 0.05 * climateHumidity;
        humidity = Math.max(0.0, Math.min(1.0, humidity));

        day++;
        if (day > 240) day = 1;
        }



    /**
     * Resets the counter for the total sunlight hours of the season.
     * <p>
     * Typically called at the end of the resting phase.</p>

     * Postconditions: totalSunlight = 0.0
     */
    //STYLE: procedural, one line reset operation.
    public void weatherTotalSunLightReset() {
        totalSunlight = 0.0;
    }

    //Getters: STYLE: Object oriented.

    /**
     * Returns the current humidity.
     * @return humidity.

     * Postconditions: the returned value is equal to current humidity.
     */
    public double getHumidity(){
        return humidity;
    }

    /**
     * Returns the current sunlight.
     * @return sunlight.

     * Postconditions: the returned value is equal to current sunlight.
     */
    public double getSunlight() {
        return sunlight;
    }

    /**
     * Returns the current totalSunlight.
     * @return totalSunlight.

     * Postconditions: the returned value is equal to current totalSunlight.
     */
    public double getTotalSunlight() {
        return totalSunlight;
    }

    /**
     * Returns the current day.
     * @return day.

     * Postconditions: the returned value is equal to current day.
     */
    public int getDay() {
        return day;
    }

}

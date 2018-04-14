using UnityEngine;
using System.Collections;
using System;
using System.Threading;

public class AIScript_NazmulRabbi : MonoBehaviour {
    public CharacterScript mainScript;
    public float[] bombSpeeds;
    public float[] buttonCooldowns;
    public float playerSpeed;
    public int[] beltDirections;
    public float[] buttonLocations;

    // My Code starts below
    // --------------------

    private float[] distance_of_bomb;
    private float location_of_player;
    private float location_of_enemy;
    private float[] value_of_btn;
    private float bot_under_pressure = 1f;
    private Thread thd;

    private const int btn_frequency = 8;
    private const int Movement = 1; 
    private const int Advance = 2; 
    private const int Analyze = 3;
    private const float Cushion = 0.1f;

    private int playerHealth = 8; 
    private int enemyHealth = 8;
    private int active = Advance;
    private int next = 0;
    private int last = -1;
    private int bombs = 0;
    private int oldBombs = 0;

    // My Code ends above
    // ------------------

	// Use this for initialization
	void Start () {
        mainScript = GetComponent<CharacterScript>();

        if (mainScript == null)
        {
            print("No CharacterScript found on " + gameObject.name);
            this.enabled = false;
        }

        buttonLocations = mainScript.getButtonLocations();
        playerSpeed = mainScript.getPlayerSpeed();

        // My Code starts below
        // --------------------

        value_of_btn = new float[btn_frequency];
        location_of_player = mainScript.getCharacterLocation();
        location_of_enemy = mainScript.getOpponentLocation();
        thd = new Thread(new ThreadStart(pathFinder));
        thd.Start();

        // My Code ends above
        // ------------------
	}

	// Update is called once per frame
	void Update () {

        buttonCooldowns = mainScript.getButtonCooldowns();
        beltDirections = mainScript.getBeltDirections();

        // My Code starts below
        // --------------------

        bombSpeeds = mainScript.getBombSpeeds();
        playerSpeed = mainScript.getPlayerSpeed();
        distance_of_bomb = mainScript.getBombDistances();
        location_of_player = mainScript.getCharacterLocation();
        location_of_enemy = mainScript.getOpponentLocation();

        if (thd.IsAlive == false){
            thd = new Thread(new ThreadStart(pathFinder));
            thd.Start();
        }

        bombs = bombsPointed();

        if (bombs != oldBombs){
            foreach (float f in distance_of_bomb){
                if (f <= 0.1f){
                    playerHealth--;
                    bot_under_pressure*=2f;
                }

                if (f >= 18.05f){
                    enemyHealth--;
                    bot_under_pressure*=0.9f;
                }
            }

            if (bombs >= playerHealth){
                bot_under_pressure*=10f; 
            }
            else{
                bot_under_pressure/=10f;
            }

            oldBombs = bombs;
        }
        
        switch (active){
            case Movement:
                MoveTowardsBtn(buttonLocations[next]);
                break;
                
            case Advance:
                if (buttonCooldowns[next] <= 0){
                    mainScript.push();
                    last = next;
                    active = Analyze;
                }

                break;
                
            case Analyze:
                next = findMax(value_of_btn);

                if (next != last){
                    active = Movement;
                }

                break;
        }

        // My Code ends above
        // ------------------
	}

    // My Methods starts below
    // -----------------------

    bool possibleToAdanvce(int i){
        float intermediary = 2.0f;

        if ((distance_of_bomb[i]/bombSpeeds[i]) > (playerButtonDistance(i)/playerSpeed) + intermediary){
            return true;
        }
        else{
            return false;
        }
        
    }

    int findMin(float[] array){
        float minValue = array[0];
        int minIndex = 0;

        for(int i = 0; i < btn_frequency; i++){
            if(array[i] < minValue){
                minValue = array[i];
                minIndex = i;
            }
        }

        return minIndex;
    }

    int findMax(float[] array){
        float maxValue = array[0];
        int maxIndex = 0;

        for(int i = 0; i < btn_frequency; i++){
            if(maxValue < array[i]){
                maxValue = array[i];
                maxIndex = i;
            }
        }

        return maxIndex;
    }

    void pathFinder(){
        float[] faraway_value = value_of_distance();

        for (int i = 0; i < btn_frequency; i++){
            value_of_btn[i] = faraway_value[i];
        }

        for (int i = 0; i < btn_frequency; i++){
            if (beltDirections[i] == 1 || possibleToAdanvce(i) == false){
                value_of_btn[i] -= 100f;
            }
        }

        for (int i = 0; i < btn_frequency; i++){
            if (beltDirections[i] == -1 && possibleToAdanvce(i) == true){
                value_of_btn[i] += (10f * (20f - distance_of_bomb[i]) * bot_under_pressure);
            }
        }

        for (int i = 0; i < btn_frequency; i++){
            if (beltDirections[i] == -1 && possibleToAdanvce(i) == true){
                value_of_btn[i] += bombSpeeds[i] * 5f;
            }
        }
    }
    
    float playerButtonDistance(int i){
        return Mathf.Abs(buttonLocations[i]-location_of_player);
    }

    float[] value_of_distance(){
        float[] Array_1 = new float[btn_frequency];
        float[] Array_2 = new float[btn_frequency];

        for(int i = 0; i < btn_frequency; i++){
            Array_2[i] = playerButtonDistance(i);
        }

        float frequency = 50f;
        for(int i = 0; i < btn_frequency; i++){
            int index = findMin(Array_2);
            Array_1[index] = frequency;
            frequency -= 5f;
            Array_2[index] = 100f;
        }

        return Array_1;
    }

    void MoveTowardsBtn(float location) {
        if(location_of_player < (location + Cushion) && location_of_player > (location - Cushion)){
            active = Advance;
        }
        else if(location_of_player < location){
          mainScript.moveUp();
        }
        else if(location_of_player > location){
          mainScript.moveDown();
        }
    }

    int bombsPointed(){
        int frequency = 0;

        foreach (int i in beltDirections){
            if (i == -1) frequency++;
        }

        return frequency;
    }

    // My Methods ends above
    // ---------------------
}
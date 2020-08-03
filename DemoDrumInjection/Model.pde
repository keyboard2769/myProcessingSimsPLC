static volatile private int vmRoller=0;

static volatile private boolean vmFeederSW = false;
static volatile private float vmFeederSettingTPH = 30f;
static volatile private float vmFeederTPHZero = 0f;
static volatile private float vmFeederTPHSpan = 60f;
static volatile private float vmFeederSettingRPM = 900f;
static volatile private float vmFeederRPMZero = 0f;
static volatile private float vmFeederRPMSpan = C_RPM_MAX;

static volatile private float vmFeederAdjustStep = 5f;

static volatile private int vmDelayTimeSEC = 10;
static volatile private int vmDelayTimeFRAME=0;
static volatile private int vmDelayTimeACTIVATOR=0;

static volatile private float vmAdditveBias  = 0.33f;
static volatile private float vmAdditveBiasL = 0.1f;
static volatile private float vmAdditveBiasH = 0.9f;

static volatile private boolean vmPumpSW = false;
static volatile private float vmPumpTPH = 50f;
static volatile private float vmPumpTPHZero = 0f;
static volatile private float vmPumpTPHSpan = 10f;
static volatile private float vmPumpRPM = 900f;
static volatile private float vmPumpRPMZero = 0f;
static volatile private float vmPumpRPMSpan = C_RPM_MAX;

//***eof
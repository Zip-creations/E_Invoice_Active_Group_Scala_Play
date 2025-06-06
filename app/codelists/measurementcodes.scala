package codelists

import utility._

import cats.data._
import cats.data.Validated._
import cats.syntax.all._


object MeasurementCode {
    def matchStr(str: String): Option[MeasurementCode] = {
        MeasurementCode.values.find(_.toString == str)
    }
    def strInList(str: String): Boolean = {
        matchStr(str).isDefined
    }
    def validate(code: String): Validated[Seq[ErrorMessage], MeasurementCode] = {
        Validated.cond(
            MeasurementCode.strInList(code),
            MeasurementCode.matchStr(code).get,
            Seq(ArgumentError)
        )
    }
}

enum MeasurementCode{
    case
        // TODO: How to include codes that start with a number?
        // 10,
        // 11,
        // 13,
        // 14,
        // 15,
        // 20,
        // 21,
        // 22,
        // 23,
        // 24,
        // 25,
        // 27,
        // 28,
        // 33,
        // 34,
        // 35,
        // 37,
        // 38,
        // 40,
        // 41,
        // 56,
        // 57,
        // 58,
        // 59,
        // 60,
        // 61,
        // 74,
        // 77,
        // 80,
        // 81,
        // 85,
        // 87,
        // 89,
        // 91,
        // 1I,
        // 2A,
        // 2B,
        // 2C,
        // 2G,
        // 2H,
        // 2I,
        // 2J,
        // 2K,
        // 2L,
        // 2M,
        // 2N,
        // 2P,
        // 2Q,
        // 2R,
        // 2U,
        // 2X,
        // 2Y,
        // 2Z,
        // 3B,
        // 3C,
        // 4C,
        // 4G,
        // 4H,
        // 4K,
        // 4L,
        // 4M,
        // 4N,
        // 4O,
        // 4P,
        // 4Q,
        // 4R,
        // 4T,
        // 4U,
        // 4W,
        // 4X,
        // 5A,
        // 5B,
        // 5E,
        // 5J,
        A10,
        A11,
        A12,
        A13,
        A14,
        A15,
        A16,
        A17,
        A18,
        A19,
        A2,
        A20,
        A21,
        A22,
        A23,
        A24,
        A26,
        A27,
        A28,
        A29,
        A3,
        A30,
        A31,
        A32,
        A33,
        A34,
        A35,
        A36,
        A37,
        A38,
        A39,
        A4,
        A40,
        A41,
        A42,
        A43,
        A44,
        A45,
        A47,
        A48,
        A49,
        A5,
        A53,
        A54,
        A55,
        A56,
        A59,
        A6,
        A68,
        A69,
        A7,
        A70,
        A71,
        A73,
        A74,
        A75,
        A76,
        A8,
        A84,
        A85,
        A86,
        A87,
        A88,
        A89,
        A9,
        A90,
        A91,
        A93,
        A94,
        A95,
        A96,
        A97,
        A98,
        A99,
        AA,
        AB,
        ACR,
        ACT,
        AD,
        AE,
        AH,
        AI,
        AK,
        AL,
        AMH,
        AMP,
        ANN,
        APZ,
        AQ,
        AS,
        ASM,
        ASU,
        ATM,
        AWG,
        AY,
        AZ,
        B1,
        B10,
        B11,
        B12,
        B13,
        B14,
        B15,
        B16,
        B17,
        B18,
        B19,
        B20,
        B21,
        B22,
        B23,
        B24,
        B25,
        B26,
        B27,
        B28,
        B29,
        B3,
        B30,
        B31,
        B32,
        B33,
        B34,
        B35,
        B4,
        B41,
        B42,
        B43,
        B44,
        B45,
        B46,
        B47,
        B48,
        B49,
        B50,
        B52,
        B53,
        B54,
        B55,
        B56,
        B57,
        B58,
        B59,
        B60,
        B61,
        B62,
        B63,
        B64,
        B66,
        B67,
        B68,
        B69,
        B7,
        B70,
        B71,
        B72,
        B73,
        B74,
        B75,
        B76,
        B77,
        B78,
        B79,
        B8,
        B80,
        B81,
        B82,
        B83,
        B84,
        B85,
        B86,
        B87,
        B88,
        B89,
        B90,
        B91,
        B92,
        B93,
        B94,
        B95,
        B96,
        B97,
        B98,
        B99,
        BAR,
        BB,
        BFT,
        BHP,
        BIL,
        BLD,
        BLL,
        BP,
        BPM,
        BQL,
        BTU,
        BUA,
        BUI,
        C0,
        C10,
        C11,
        C12,
        C13,
        C14,
        C15,
        C16,
        C17,
        C18,
        C19,
        C20,
        C21,
        C22,
        C23,
        C24,
        C25,
        C26,
        C27,
        C28,
        C29,
        C3,
        C30,
        C31,
        C32,
        C33,
        C34,
        C35,
        C36,
        C37,
        C38,
        C39,
        C40,
        C41,
        C42,
        C43,
        C44,
        C45,
        C46,
        C47,
        C48,
        C49,
        C50,
        C51,
        C52,
        C53,
        C54,
        C55,
        C56,
        C57,
        C58,
        C59,
        C60,
        C61,
        C62,
        C63,
        C64,
        C65,
        C66,
        C67,
        C68,
        C69,
        C7,
        C70,
        C71,
        C72,
        C73,
        C74,
        C75,
        C76,
        C78,
        C79,
        C8,
        C80,
        C81,
        C82,
        C83,
        C84,
        C85,
        C86,
        C87,
        C88,
        C89,
        C9,
        C90,
        C91,
        C92,
        C93,
        C94,
        C95,
        C96,
        C97,
        C99,
        CCT,
        CDL,
        CEL,
        CEN,
        CG,
        CGM,
        CKG,
        CLF,
        CLT,
        CMK,
        CMQ,
        CMT,
        CNP,
        CNT,
        COU,
        CTG,
        CTM,
        CTN,
        CUR,
        CWA,
        CWI,
        D03,
        D04,
        D1,
        D10,
        D11,
        D12,
        D13,
        D15,
        D16,
        D17,
        D18,
        D19,
        D2,
        D20,
        D21,
        D22,
        D23,
        D24,
        D25,
        D26,
        D27,
        D29,
        D30,
        D31,
        D32,
        D33,
        D34,
        D36,
        D41,
        D42,
        D43,
        D44,
        D45,
        D46,
        D47,
        D48,
        D49,
        D5,
        D50,
        D51,
        D52,
        D53,
        D54,
        D55,
        D56,
        D57,
        D58,
        D59,
        D6,
        D60,
        D61,
        D62,
        D63,
        D65,
        D68,
        D69,
        D73,
        D74,
        D77,
        D78,
        D80,
        D81,
        D82,
        D83,
        D85,
        D86,
        D87,
        D88,
        D89,
        D91,
        D93,
        D94,
        D95,
        DAA,
        DAD,
        DAY,
        DB,
        DBM,
        DBW,
        DD,
        DEC,
        DG,
        DJ,
        DLT,
        DMA,
        DMK,
        DMO,
        DMQ,
        DMT,
        DN,
        DPC,
        DPR,
        DPT,
        DRA,
        DRI,
        DRL,
        DT,
        DTN,
        DWT,
        DZN,
        DZP,
        E01,
        E07,
        E08,
        E09,
        E10,
        E12,
        E14,
        E15,
        E16,
        E17,
        E18,
        E19,
        E20,
        E21,
        E22,
        E23,
        E25,
        E27,
        E28,
        E30,
        E31,
        E32,
        E33,
        E34,
        E35,
        E36,
        E37,
        E38,
        E39,
        E4,
        E40,
        E41,
        E42,
        E43,
        E44,
        E45,
        E46,
        E47,
        E48,
        E49,
        E50,
        E51,
        E52,
        E53,
        E54,
        E55,
        E56,
        E57,
        E58,
        E59,
        E60,
        E61,
        E62,
        E63,
        E64,
        E65,
        E66,
        E67,
        E68,
        E69,
        E70,
        E71,
        E72,
        E73,
        E74,
        E75,
        E76,
        E77,
        E78,
        E79,
        E80,
        E81,
        E82,
        E83,
        E84,
        E85,
        E86,
        E87,
        E88,
        E89,
        E90,
        E91,
        E92,
        E93,
        E94,
        E95,
        E96,
        E97,
        E98,
        E99,
        EA,
        EB,
        EQ,
        F01,
        F02,
        F03,
        F04,
        F05,
        F06,
        F07,
        F08,
        F10,
        F11,
        F12,
        F13,
        F14,
        F15,
        F16,
        F17,
        F18,
        F19,
        F20,
        F21,
        F22,
        F23,
        F24,
        F25,
        F26,
        F27,
        F28,
        F29,
        F30,
        F31,
        F32,
        F33,
        F34,
        F35,
        F36,
        F37,
        F38,
        F39,
        F40,
        F41,
        F42,
        F43,
        F44,
        F45,
        F46,
        F47,
        F48,
        F49,
        F50,
        F51,
        F52,
        F53,
        F54,
        F55,
        F56,
        F57,
        F58,
        F59,
        F60,
        F61,
        F62,
        F63,
        F64,
        F65,
        F66,
        F67,
        F68,
        F69,
        F70,
        F71,
        F72,
        F73,
        F74,
        F75,
        F76,
        F77,
        F78,
        F79,
        F80,
        F81,
        F82,
        F83,
        F84,
        F85,
        F86,
        F87,
        F88,
        F89,
        F90,
        F91,
        F92,
        F93,
        F94,
        F95,
        F96,
        F97,
        F98,
        F99,
        FAH,
        FAR,
        FBM,
        FC,
        FF,
        FH,
        FIT,
        FL,
        FNU,
        FOT,
        FP,
        FR,
        FS,
        FTK,
        FTQ,
        G01,
        G04,
        G05,
        G06,
        G08,
        G09,
        G10,
        G11,
        G12,
        G13,
        G14,
        G15,
        G16,
        G17,
        G18,
        G19,
        G2,
        G20,
        G21,
        G23,
        G24,
        G25,
        G26,
        G27,
        G28,
        G29,
        G3,
        G30,
        G31,
        G32,
        G33,
        G34,
        G35,
        G36,
        G37,
        G38,
        G39,
        G40,
        G41,
        G42,
        G43,
        G44,
        G45,
        G46,
        G47,
        G48,
        G49,
        G50,
        G51,
        G52,
        G53,
        G54,
        G55,
        G56,
        G57,
        G58,
        G59,
        G60,
        G61,
        G62,
        G63,
        G64,
        G65,
        G66,
        G67,
        G68,
        G69,
        G70,
        G71,
        G72,
        G73,
        G74,
        G75,
        G76,
        G77,
        G78,
        G79,
        G80,
        G81,
        G82,
        G83,
        G84,
        G85,
        G86,
        G87,
        G88,
        G89,
        G90,
        G91,
        G92,
        G93,
        G94,
        G95,
        G96,
        G97,
        G98,
        G99,
        GB,
        GBQ,
        GDW,
        GE,
        GF,
        GFI,
        GGR,
        GIA,
        GIC,
        GII,
        GIP,
        GJ,
        GL,
        GLD,
        GLI,
        GLL,
        GM,
        GO,
        GP,
        GQ,
        GRM,
        GRN,
        GRO,
        GV,
        GWH,
        H03,
        H04,
        H05,
        H06,
        H07,
        H08,
        H09,
        H10,
        H11,
        H12,
        H13,
        H14,
        H15,
        H16,
        H18,
        H19,
        H20,
        H21,
        H22,
        H23,
        H24,
        H25,
        H26,
        H27,
        H28,
        H29,
        H30,
        H31,
        H32,
        H33,
        H34,
        H35,
        H36,
        H37,
        H38,
        H39,
        H40,
        H41,
        H42,
        H43,
        H44,
        H45,
        H46,
        H47,
        H48,
        H49,
        H50,
        H51,
        H52,
        H53,
        H54,
        H55,
        H56,
        H57,
        H58,
        H59,
        H60,
        H61,
        H62,
        H63,
        H64,
        H65,
        H66,
        H67,
        H68,
        H69,
        H70,
        H71,
        H72,
        H73,
        H74,
        H75,
        H76,
        H77,
        H79,
        H80,
        H81,
        H82,
        H83,
        H84,
        H85,
        H87,
        H88,
        H89,
        H90,
        H91,
        H92,
        H93,
        H94,
        H95,
        H96,
        H98,
        H99,
        HA,
        HAD,
        HBA,
        HBX,
        HC,
        HDW,
        HEA,
        HGM,
        HH,
        HIU,
        HKM,
        HLT,
        HM,
        HMO,
        HMQ,
        HMT,
        HPA,
        HTZ,
        HUR,
        HWE,
        IA,
        IE,
        INH,
        INK,
        INQ,
        ISD,
        IU,
        IUG,
        IV,
        J10,
        J12,
        J13,
        J14,
        J15,
        J16,
        J17,
        J18,
        J19,
        J2,
        J20,
        J21,
        J22,
        J23,
        J24,
        J25,
        J26,
        J27,
        J28,
        J29,
        J30,
        J31,
        J32,
        J33,
        J34,
        J35,
        J36,
        J38,
        J39,
        J40,
        J41,
        J42,
        J43,
        J44,
        J45,
        J46,
        J47,
        J48,
        J49,
        J50,
        J51,
        J52,
        J53,
        J54,
        J55,
        J56,
        J57,
        J58,
        J59,
        J60,
        J61,
        J62,
        J63,
        J64,
        J65,
        J66,
        J67,
        J68,
        J69,
        J70,
        J71,
        J72,
        J73,
        J74,
        J75,
        J76,
        J78,
        J79,
        J81,
        J82,
        J83,
        J84,
        J85,
        J87,
        J90,
        J91,
        J92,
        J93,
        J95,
        J96,
        J97,
        J98,
        J99,
        JE,
        JK,
        JM,
        JNT,
        JOU,
        JPS,
        JWL,
        K1,
        K10,
        K11,
        K12,
        K13,
        K14,
        K15,
        K16,
        K17,
        K18,
        K19,
        K2,
        K20,
        K21,
        K22,
        K23,
        K26,
        K27,
        K28,
        K3,
        K30,
        K31,
        K32,
        K33,
        K34,
        K35,
        K36,
        K37,
        K38,
        K39,
        K40,
        K41,
        K42,
        K43,
        K45,
        K46,
        K47,
        K48,
        K49,
        K50,
        K51,
        K52,
        K53,
        K54,
        K55,
        K58,
        K59,
        K6,
        K60,
        K61,
        K62,
        K63,
        K64,
        K65,
        K66,
        K67,
        K68,
        K69,
        K70,
        K71,
        K73,
        K74,
        K75,
        K76,
        K77,
        K78,
        K79,
        K80,
        K81,
        K82,
        K83,
        K84,
        K85,
        K86,
        K87,
        K88,
        K89,
        K90,
        K91,
        K92,
        K93,
        K94,
        K95,
        K96,
        K97,
        K98,
        K99,
        KA,
        KAT,
        KB,
        KBA,
        KCC,
        KDW,
        KEL,
        KGM,
        KGS,
        KHY,
        KHZ,
        KI,
        KIC,
        KIP,
        KJ,
        KJO,
        KL,
        KLK,
        KLX,
        KMA,
        KMH,
        KMK,
        KMQ,
        KMT,
        KNI,
        KNM,
        KNS,
        KNT,
        KO,
        KPA,
        KPH,
        KPO,
        KPP,
        KR,
        KSD,
        KSH,
        KT,
        KTN,
        KUR,
        KVA,
        KVR,
        KVT,
        KW,
        KWH,
        KWN,
        KWO,
        KWS,
        KWT,
        KWY,
        KX,
        L10,
        L11,
        L12,
        L13,
        L14,
        L15,
        L16,
        L17,
        L18,
        L19,
        L2,
        L20,
        L21,
        L23,
        L24,
        L25,
        L26,
        L27,
        L28,
        L29,
        L30,
        L31,
        L32,
        L33,
        L34,
        L35,
        L36,
        L37,
        L38,
        L39,
        L40,
        L41,
        L42,
        L43,
        L44,
        L45,
        L46,
        L47,
        L48,
        L49,
        L50,
        L51,
        L52,
        L53,
        L54,
        L55,
        L56,
        L57,
        L58,
        L59,
        L60,
        L63,
        L64,
        L65,
        L66,
        L67,
        L68,
        L69,
        L70,
        L71,
        L72,
        L73,
        L74,
        L75,
        L76,
        L77,
        L78,
        L79,
        L80,
        L81,
        L82,
        L83,
        L84,
        L85,
        L86,
        L87,
        L88,
        L89,
        L90,
        L91,
        L92,
        L93,
        L94,
        L95,
        L96,
        L98,
        L99,
        LA,
        LAC,
        LBR,
        LBT,
        LD,
        LEF,
        LF,
        LH,
        LK,
        LM,
        LN,
        LO,
        LP,
        LPA,
        LR,
        LS,
        LTN,
        LTR,
        LUB,
        LUM,
        LUX,
        LY,
        M1,
        M10,
        M11,
        M12,
        M13,
        M14,
        M15,
        M16,
        M17,
        M18,
        M19,
        M20,
        M21,
        M22,
        M23,
        M24,
        M25,
        M26,
        M27,
        M29,
        M30,
        M31,
        M32,
        M33,
        M34,
        M35,
        M36,
        M37,
        M38,
        M39,
        M4,
        M40,
        M41,
        M42,
        M43,
        M44,
        M45,
        M46,
        M47,
        M48,
        M49,
        M5,
        M50,
        M51,
        M52,
        M53,
        M55,
        M56,
        M57,
        M58,
        M59,
        M60,
        M61,
        M62,
        M63,
        M64,
        M65,
        M66,
        M67,
        M68,
        M69,
        M7,
        M70,
        M71,
        M72,
        M73,
        M74,
        M75,
        M76,
        M77,
        M78,
        M79,
        M80,
        M81,
        M82,
        M83,
        M84,
        M85,
        M86,
        M87,
        M88,
        M89,
        M9,
        M90,
        M91,
        M92,
        M93,
        M94,
        M95,
        M96,
        M97,
        M98,
        M99,
        MAH,
        MAL,
        MAM,
        MAR,
        MAW,
        MBE,
        MBF,
        MBR,
        MC,
        MCU,
        MD,
        MGM,
        MHZ,
        MIK,
        MIL,
        MIN,
        MIO,
        MIU,
        MKD,
        MKM,
        MKW,
        MLD,
        MLT,
        MMK,
        MMQ,
        MMT,
        MND,
        MNJ,
        MON,
        MPA,
        MQD,
        MQH,
        MQM,
        MQS,
        MQW,
        MRD,
        MRM,
        MRW,
        MSK,
        MTK,
        MTQ,
        MTR,
        MTS,
        MTZ,
        MVA,
        MWH,
        N1,
        N10,
        N11,
        N12,
        N13,
        N14,
        N15,
        N16,
        N17,
        N18,
        N19,
        N20,
        N21,
        N22,
        N23,
        N24,
        N25,
        N26,
        N27,
        N28,
        N29,
        N3,
        N30,
        N31,
        N32,
        N33,
        N34,
        N35,
        N36,
        N37,
        N38,
        N39,
        N40,
        N41,
        N42,
        N43,
        N44,
        N45,
        N46,
        N47,
        N48,
        N49,
        N50,
        N51,
        N52,
        N53,
        N54,
        N55,
        N56,
        N57,
        N58,
        N59,
        N60,
        N61,
        N62,
        N63,
        N64,
        N65,
        N66,
        N67,
        N68,
        N69,
        N70,
        N71,
        N72,
        N73,
        N74,
        N75,
        N76,
        N77,
        N78,
        N79,
        N80,
        N81,
        N82,
        N83,
        N84,
        N85,
        N86,
        N87,
        N88,
        N89,
        N90,
        N91,
        N92,
        N93,
        N94,
        N95,
        N96,
        N97,
        N98,
        N99,
        NA,
        NAR,
        NCL,
        NEW,
        NF,
        NIL,
        NIU,
        NL,
        NM3,
        NMI,
        NMP,
        NPT,
        NT,
        NTU,
        NU,
        NX,
        OA,
        ODE,
        ODG,
        ODK,
        ODM,
        OHM,
        ON,
        ONZ,
        OPM,
        OT,
        OZA,
        OZI,
        P1,
        P10,
        P11,
        P12,
        P13,
        P14,
        P15,
        P16,
        P17,
        P18,
        P19,
        P2,
        P20,
        P21,
        P22,
        P23,
        P24,
        P25,
        P26,
        P27,
        P28,
        P29,
        P30,
        P31,
        P32,
        P33,
        P34,
        P35,
        P36,
        P37,
        P38,
        P39,
        P40,
        P41,
        P42,
        P43,
        P44,
        P45,
        P46,
        P47,
        P48,
        P49,
        P5,
        P50,
        P51,
        P52,
        P53,
        P54,
        P55,
        P56,
        P57,
        P58,
        P59,
        P60,
        P61,
        P62,
        P63,
        P64,
        P65,
        P66,
        P67,
        P68,
        P69,
        P70,
        P71,
        P72,
        P73,
        P74,
        P75,
        P76,
        P77,
        P78,
        P79,
        P80,
        P81,
        P82,
        P83,
        P84,
        P85,
        P86,
        P87,
        P88,
        P89,
        P90,
        P91,
        P92,
        P93,
        P94,
        P95,
        P96,
        P97,
        P98,
        P99,
        PAL,
        PD,
        PFL,
        PGL,
        PI,
        PLA,
        PO,
        PQ,
        PR,
        PS,
        PTD,
        PTI,
        PTL,
        PTN,
        Q10,
        Q11,
        Q12,
        Q13,
        Q14,
        Q15,
        Q16,
        Q17,
        Q18,
        Q19,
        Q20,
        Q21,
        Q22,
        Q23,
        Q24,
        Q25,
        Q26,
        Q27,
        Q28,
        Q29,
        Q3,
        Q30,
        Q31,
        Q32,
        Q33,
        Q34,
        Q35,
        Q36,
        Q37,
        Q38,
        Q39,
        Q40,
        Q41,
        Q42,
        QA,
        QAN,
        QB,
        QR,
        QTD,
        QTI,
        QTL,
        QTR,
        R1,
        R9,
        RH,
        RM,
        ROM,
        RP,
        RPM,
        RPS,
        RT,
        S3,
        S4,
        SAN,
        SCO,
        SCR,
        SEC,
        SET,
        SG,
        SIE,
        SM3,
        SMI,
        SQ,
        SQR,
        SR,
        STC,
        STI,
        STK,
        STL,
        STN,
        STW,
        SW,
        SX,
        SYR,
        T0,
        T3,
        TAH,
        TAN,
        TI,
        TIC,
        TIP,
        TKM,
        TMS,
        TNE,
        TP,
        TPI,
        TPR,
        TQD,
        TRL,
        TST,
        TTS,
        U1,
        U2,
        UB,
        UC,
        VA,
        VLT,
        VP,
        W2,
        WA,
        WB,
        WCD,
        WE,
        WEB,
        WEE,
        WG,
        WHR,
        WM,
        WSD,
        WTT,
        X1,
        YDK,
        YDQ,
        YRD,
        Z11,
        Z9,
        ZP,
        ZZ,
}

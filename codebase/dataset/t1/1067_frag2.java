                    SolutionSet nonDominatedTmp = (new Ranking(archive)).getSubfront(0);

                    bw.write(evaluations + "");

                    bw.write(" ");

                    bw.write(indicators.getParetoOptimalSolutions(nonDominatedTmp) + "");

                    bw.write(" ");

                    bw.write(indicators.getGD(nonDominatedTmp) + "");

                    bw.write(" ");

                    bw.write(indicators.getIGD(nonDominatedTmp) + "");

                    bw.write(" ");

                    bw.write(indicators.getEpsilon(nonDominatedTmp) + "");

                    bw.write(" ");

                    bw.write(indicators.getSpread(nonDominatedTmp) + "");

                    bw.write(" ");

                    bw.write(indicators.getHypervolume(nonDominatedTmp) + "");

                    bw.newLine();

                }

            }
